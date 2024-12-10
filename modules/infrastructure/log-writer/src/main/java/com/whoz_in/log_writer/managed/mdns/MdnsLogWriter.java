package com.whoz_in.log_writer.managed.mdns;

import com.whoz_in.log_writer.config.NetworkConfig;
import com.whoz_in.log_writer.managed.ManagedInfo;
import com.whoz_in.log_writer.managed.ManagedLog;
import com.whoz_in.log_writer.managed.ManagedLogDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//TODO: tshark가 종료되지 않더라도 오류가 발생할 수 있으려나? 이 경우에도 로깅이 필요하긴 함
@Slf4j
@Component
public class MdnsLogWriter {
    private final Map<ManagedInfo, MdnsLogProcess> processes;
    private final Map<ManagedInfo, Boolean> wasDead;
    private final MdnsLogParser parser;
    private final ManagedLogDAO dao;
    private final String sudoPassword;

    public MdnsLogWriter(ManagedLogDAO dao, MdnsLogParser parser, NetworkConfig config, @Value("${sudo_password}") String sudoPassword) {
        this.dao = dao;
        this.parser = parser;
        this.sudoPassword = sudoPassword;
        this.processes = new HashMap<>();
        this.wasDead = new HashMap<>();
        config.getMdnsList().parallelStream()
                .forEach(managedInfo -> {
                    this.processes.put(managedInfo, new MdnsLogProcess(managedInfo, sudoPassword));
                    log.info("[managed - mdns({})] started", managedInfo.ssid());
                    this.wasDead.put(managedInfo, false);
                });
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    private void writeLogs() {
        List<ManagedLog> totalLogs = this.processes.entrySet().parallelStream()
                .filter(entry-> {
                    ManagedInfo managedInfo = entry.getKey();
                    MdnsLogProcess process = entry.getValue();
                    boolean alive = process.isAlive();
                    if (!alive && wasDead.get(managedInfo).equals(Boolean.FALSE)) {
                        wasDead.put(managedInfo, true);
                        log.error("[managed - mdns({})] dead :\n{}\n{}", managedInfo.ssid(), "에러 스트림 내용:", process.readErrorLines());
                    }
                    return alive;})
                .flatMap(entry -> {
                    ManagedInfo managedInfo = entry.getKey();
                    MdnsLogProcess process = entry.getValue();
                    Map<ManagedLog, ManagedLog> logs = new HashMap<>();
                    String line;
                    for(;;) {
                        line = process.readLine();
                        if (line == null) {
                            log.info("[managed - mdns({})] log to save : {}", managedInfo.ssid(),  logs.size());
                            return logs.values().stream();
                        }
                        parser.parse(line).ifPresent(
                                mdnsLog -> {
                                    logs.put(mdnsLog, mdnsLog);
                                    mdnsLog.setSsid(managedInfo.ssid());}
                        );
                    }
                })
                .toList();

        dao.upsertAll(totalLogs);
    }

}
