package com.whoz_in.network_log.infra.managed.mdns;

import com.whoz_in.network_log.persistence.ManagedLog;
import com.whoz_in.network_log.persistence.ManagedLogDAO;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MdnsLogWriter {
    private final List<MdnsLogProcess> processes;
    private final MdnsLogParser parser;
    private final ManagedLogDAO managedLogDAO;
    private final MdnsConfig config; //재시동에 필요

    public MdnsLogWriter(ManagedLogDAO managedLogDAO, MdnsLogParser parser, MdnsConfig config) {
        this.managedLogDAO = managedLogDAO;
        this.parser = parser;
        this.config = config;
        this.processes = config.getMDnsCommands().parallelStream()
                .map(commands -> new MdnsLogProcess(commands, config.getPassword())
                ).toList();
    }

    //TODO: 프로세스 살아있는지 확인하고 재시동

    @Scheduled(fixedRate = 10000)
    private void writeLogs() {
        Set<MdnsLog> logs = new HashSet<>();
        this.processes.parallelStream()
                        .forEach(process-> {
                            String line;
                            for(;;) {
                                try {
                                    line = process.readLine();
                                    if (line == null) return;
                                    parser.parse(line).ifPresent(logs::add);
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
        System.out.println("[managed] 저장할 로그 개수 : " + logs.size());
        //TODO: jpa entity -> native query
        //TODO: ssid 저장
        Set<ManagedLog> entities = logs.stream().map(ManagedLog::create).collect(Collectors.toSet());
        managedLogDAO.bulkInsert(entities);
        logs.clear();
    }

}
