package com.whoz_in.log_writer.monitor;

import com.whoz_in.log_writer.common.SystemNetworkInterfaces;
import com.whoz_in.log_writer.config.NetworkConfig;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MonitorLogWriter {
    private MonitorLogProcess process; //교체될 수 있으므로 final X
    private boolean isProcDead;
    private final MonitorLogParser parser;
    private final MonitorLogDAO dao;
    private final String sudoPassword;
    private final MonitorInfo monitorInfo;
    private final SystemNetworkInterfaces systemNIs;

    public MonitorLogWriter(MonitorLogParser parser, MonitorLogDAO dao, NetworkConfig config,
            @Value("${sudo_password}") String sudoPassword,
            SystemNetworkInterfaces systemNIs) {
        this.parser = parser;
        this.dao = dao;
        this.monitorInfo = config.getMonitorInfo();
        this.sudoPassword = sudoPassword;
        this.systemNIs = systemNIs;
        startProcess();
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    private void saveLogs(){
        //프로세스 죽었으면 기록 안함
        if (!process.isAlive()) return;

        Set<String> macs = new HashSet<>();
        process.readLines().stream()
                .map(parser::parse)
                .forEach(macs::addAll);
        macs.remove("");

        log.info("[monitor] mac to save: " + macs.size());
        dao.upsertAll(macs);
    }

    //프로세스 죽었으면 에러 내용을 기록하고 프로세스 재실행
    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    private void checkProcess(){
        if (process.isAlive()) return;

        if (!isProcDead) {
            isProcDead = true;
            log.error("[monitor] 프로세스가 종료됨 :\n{}\n{}", "프로세스의 에러 스트림 내용:", process.readErrorLines());
        }
        startProcess();
    }

    //이전 프로세스를 제거하고 새로운 프로세스를 실행시켜 교체하는 역할
    private void startProcess(){
        log.info("[monitor] 프로세스를 실행합니다.");

        //TODO: validator
        if (!systemNIs.exists(monitorInfo.ni())){
            log.error("[monitor] 실행 실패 : 설정된 모니터 네트워크 인터페이스가 시스템에 존재하지 않습니다.");
            return;
        }
        Optional.ofNullable(this.process).ifPresent(MonitorLogProcess::terminate);
        this.process = new MonitorLogProcess(monitorInfo, sudoPassword);
        this.isProcDead = false;

        log.info("[monitor] 프로세스 실행 완료");
    }

}