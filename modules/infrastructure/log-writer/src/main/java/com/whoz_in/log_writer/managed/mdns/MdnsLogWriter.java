package com.whoz_in.log_writer.managed.mdns;

import com.whoz_in.log_writer.common.SystemNetworkInterfaces;
import com.whoz_in.log_writer.config.NetworkConfig;
import com.whoz_in.log_writer.managed.ManagedInfo;
import com.whoz_in.log_writer.managed.ManagedLog;
import com.whoz_in.log_writer.managed.ManagedLogDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//TODO: tshark가 종료되지 않더라도 오류가 발생할 수 있으려나? 이 경우에도 로깅이 필요하긴 함
@Slf4j
@Component
public class MdnsLogWriter {
    private final Map<ManagedInfo, MdnsLogProcess> processes;
    private final Map<ManagedInfo, Boolean> dead;
    private final MdnsLogParser parser;
    private final ManagedLogDAO dao;
    private final String sudoPassword;
    private final SystemNetworkInterfaces systemNIs;

    public MdnsLogWriter(ManagedLogDAO dao, MdnsLogParser parser, NetworkConfig config,
            @Value("${sudo_password}") String sudoPassword,
            SystemNetworkInterfaces systemNIs
    ) {
        this.dao = dao;
        this.parser = parser;
        this.sudoPassword = sudoPassword;
        this.processes = new HashMap<>();
        this.dead = new HashMap<>();
        this.systemNIs = systemNIs;
        config.getMdnsList().forEach(this::startProcess);
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    private void saveLogs() {
        List<ManagedLog> totalLogs = this.processes.entrySet().parallelStream()
                .filter(entry-> entry.getValue().isAlive())
                .flatMap(entry -> {
                    ManagedInfo managedInfo = entry.getKey();
                    MdnsLogProcess process = entry.getValue();
                    Map<ManagedLog, ManagedLog> logs = new HashMap<>();
                    String line;
                    for(;;) {
                        line = process.readLine();
                        if (line == null) {
                            log.info("[managed - mdns({})] log to save : {}", managedInfo.ni().getEssid(),  logs.size());
                            return logs.values().stream();
                        }
                        parser.parse(line).ifPresent(
                                mdnsLog -> {
                                    logs.put(mdnsLog, mdnsLog);
                                    mdnsLog.setSsid(managedInfo.ni().getEssid());}
                        );
                    }
                })
                .toList();

        dao.upsertAll(totalLogs);
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    private void checkProcesses(){
        this.processes.entrySet().parallelStream()
                .filter(entry-> !entry.getValue().isAlive())
                .forEach(entry ->{
                    ManagedInfo managedInfo = entry.getKey();
                    MdnsLogProcess process = entry.getValue();
                    //프로세스가 죽었다는 것을 인지했을때만 아래 로직을 실행
                    if (dead.get(managedInfo).equals(Boolean.FALSE)) {
                        dead.put(managedInfo, true);
                        log.error("[managed - mdns({})] 프로세스가 종료됨 :\n{}\n{}", managedInfo.ni().getEssid(), "프로세스의 에러 스트림 내용:", process.readErrorLines());
                    }
                    startProcess(managedInfo);
                });
    }

    private void startProcess(ManagedInfo info){
        String ssid = info.ni().getEssid();
        log.info("[managed - mdns({})] 프로세스를 실행합니다.", ssid);

        if (!systemNIs.exists(info.ni())){
            log.error("[managed - mdns({})] 설정된 mdns 네트워크 인터페이스가 시스템에 존재하지 않습니다.", ssid);
            return;
        }
        Optional.ofNullable(this.processes.get(info)).ifPresent(MdnsLogProcess::terminate);
        MdnsLogProcess process = new MdnsLogProcess(info, sudoPassword);
        this.processes.put(info, process);
        this.dead.put(info, false);

        log.info("[managed - mdns({})] 프로세스 실행 완료", ssid);
    }


}
