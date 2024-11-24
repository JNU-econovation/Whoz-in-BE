package com.whoz_in.network_log.infra.managed.arp;

import com.whoz_in.network_log.persistence.ManagedLog;
import com.whoz_in.network_log.persistence.ManagedLogDAO;
import com.whoz_in.network_log.persistence.ManagedLogRepository;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ArpLogWriter {

    private final ManagedLogRepository repository;
    private final ManagedLogDAO managedLogDAO;
    private final ArpLogParser arpLogParser;
    private final ArpConfig arpConfig;

    private final Set<String> logs = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public ArpLogWriter(ManagedLogRepository repository,
                        ManagedLogDAO managedLogDAO,
                        ArpLogParser arpLogParser,
                        ArpConfig arpConfig) {
        this.repository = repository;
        this.managedLogDAO = managedLogDAO;
        this.arpLogParser = arpLogParser;
        this.arpConfig = arpConfig;
    }

    @PostConstruct
    private void init() {
        CompletableFuture<Set<String>> logCollect =
                CompletableFuture.supplyAsync(() -> {
                    ArpLogProcess arpLogProcess =
                            new ArpLogProcess(
                            arpConfig.getCommand(),
                            arpConfig.getPassword());

                    return arpLogProcess.start();
                });

        Set<String> result = logCollect.join();
        result.forEach(System.out::println);

        logs.addAll(result);
    }

    //TODO: LogEntity들 저장
    private void save(){
        Set<ManagedLog> managedLogs = logs.stream()
                .map(arpLogParser::parse)
                .map(opt -> {
                    return ManagedLog.create(opt.orElse(null));
                })
                .collect(Collectors.toSet());

        managedLogDAO.bulkInsert(managedLogs);
    }

}
