package com.whoz_in.log_writer.monitor;

import com.whoz_in.log_writer.config.NetworkConfig;
import com.whoz_in.log_writer.managed.ManagedInfo;
import com.whoz_in.log_writer.managed.mdns.MdnsLogProcess;
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
    private Boolean dead = false;
    private final MonitorLogParser parser;
    private final MonitorLogDAO repo;
    private final String sudoPassword;
    private final MonitorInfo monitorInfo;

    public MonitorLogWriter(MonitorLogParser parser, MonitorLogDAO repo, NetworkConfig config, @Value("${sudo_password}") String sudoPassword) {
        this.parser = parser;
        this.repo = repo;
        this.monitorInfo = config.getMonitorInfo();
        this.sudoPassword = sudoPassword;
        startProcess();
    }
    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    private void saveLogs(){
        if (!process.isAlive()) {
            if (dead.equals(Boolean.FALSE)) {
                dead = true;
                log.error("[monitor] 프로세스가 종료됨 :\n{}\n{}", "프로세스의 에러 스트림 내용:", process.readErrorLines());
                log.error("[monitor] 프로세스를 재실행합니다.");
                startProcess();
            }
            return;
        }
        Set<String> macs = new HashSet<>();
        String line;
        for(;;) {
            line = process.readLine();
            if (line == null) break;
            macs.addAll(parser.parse(line));
        }
        macs.remove("");

        log.info("[monitor] mac to save: " + macs.size());
        repo.upsertAll(macs);
    }

    private void startProcess(){
        Optional.ofNullable(this.process).ifPresent(MonitorLogProcess::terminate);
        MonitorLogProcess p = new MonitorLogProcess(monitorInfo, sudoPassword);
        this.process = p;
        log.info("[monitor] 프로세스 실행 완료");
        this.dead = false;
    }

}