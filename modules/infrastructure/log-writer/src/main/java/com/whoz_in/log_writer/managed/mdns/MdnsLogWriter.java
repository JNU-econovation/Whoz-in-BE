package com.whoz_in.log_writer.managed.mdns;

import com.whoz_in.log_writer.config.NetworkConfig;
import com.whoz_in.log_writer.managed.ManagedInfo;
import com.whoz_in.log_writer.managed.ManagedLog;
import com.whoz_in.log_writer.managed.ManagedLogDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MdnsLogWriter {
    private final Map<ManagedInfo, MdnsLogProcess> processes;
    private final MdnsLogParser parser;
    private final ManagedLogDAO dao;
    private final String sudoPassword;

    public MdnsLogWriter(ManagedLogDAO dao, MdnsLogParser parser, NetworkConfig config, @Value("${sudo_password}") String sudoPassword) {
        this.dao = dao;
        this.parser = parser;
        this.sudoPassword = sudoPassword;
        this.processes = config.getMdnsList().parallelStream()
                .collect(Collectors.toMap(
                        managedInfo -> managedInfo,
                        managedInfo -> new MdnsLogProcess(managedInfo, sudoPassword)
                ));
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    private void writeLogs() {
        List<ManagedLog> totalLogs = this.processes.entrySet().parallelStream()
                .filter(entry-> {
                    ManagedInfo managedInfo = entry.getKey();
                    MdnsLogProcess process = entry.getValue();
                    boolean alive = process.isAlive();
                    if (!alive) System.err.println("[managed - mdns(%s)] 종료됨 : ERROR".formatted(managedInfo.ssid()));
                    return alive;})
                .flatMap(entry -> {
                    ManagedInfo managedInfo = entry.getKey();
                    MdnsLogProcess process = entry.getValue();
                    Map<ManagedLog, ManagedLog> logs = new HashMap<>();
                    String line;
                    for(;;) {
                        line = process.readLine();
                        System.out.println("            mdns(%s): ".formatted(managedInfo.ssid()) + line);
                        if (line == null) {
                            System.out.println("[managed - mdns(%s)] 저장할 로그 개수 : ".formatted(
                                    managedInfo.ssid()) + logs.size());
                            return logs.values().stream();
                        }
                        parser.parse(line).ifPresent(
                                log -> {
                                    logs.put(log, log);
                                    log.setSsid(managedInfo.ssid());}
                        );
                    }
                })
                .toList();

        dao.upsertAll(totalLogs);
    }

}
