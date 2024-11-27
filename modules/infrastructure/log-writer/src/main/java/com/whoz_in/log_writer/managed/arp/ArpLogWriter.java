package com.whoz_in.log_writer.managed.arp;


import com.whoz_in.log_writer.managed.ManagedLog;
import com.whoz_in.log_writer.managed.ManagedLogDAO;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ArpLogWriter {

    private final ManagedLogDAO managedLogDAO;
    private final ArpLogParser arpLogParser;
    private final ArpLogProcess arpLogProcess;

    private final Set<String> logs = Collections.newSetFromMap(new ConcurrentHashMap<>()); //TODO: 일반 HashSet으로 변경

    public ArpLogWriter(ManagedLogDAO managedLogDAO,
                        ArpLogParser arpLogParser,
                        ArpLogProcess arpLogProcess) {
        this.managedLogDAO = managedLogDAO;
        this.arpLogParser = arpLogParser;
        this.arpLogProcess = arpLogProcess;
    }

    @Scheduled(fixedRate = 5000)
    private void scan() {
        logs.addAll(arpLogProcess.run());

        save();
    }

    private void save(){
        Set<ManagedLog> managedLogs = logs.stream()
                .filter(arpLogParser::validate)
                .map(arpLogParser::parse)
                .collect(Collectors.toSet());

        System.out.println(String.format("[managed - arp] 저장할 로그 개수 : %d", managedLogs.size()));

        managedLogDAO.insertAll(managedLogs);
    }

}
