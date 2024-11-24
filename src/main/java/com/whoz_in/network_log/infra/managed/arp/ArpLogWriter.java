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
import org.hibernate.engine.spi.Managed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ArpLogWriter {

    private final ManagedLogRepository repository;
    private final ManagedLogDAO managedLogDAO;

    @Value("${network.process.command.managed.arp}")
    private String command;

    @Value("${network.process.password}")
    private String password;

    private final Set<String> logs = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public ArpLogWriter(ManagedLogRepository repository,
                        ManagedLogDAO managedLogDAO) {
        this.repository = repository;
        this.managedLogDAO = managedLogDAO;
    }

    @PostConstruct
    private void init() {
        CompletableFuture<Set<String>> logCollect =
                CompletableFuture.supplyAsync(() -> {
                    ArpLogProcess arpLogProcess = new ArpLogProcess(command, password);
                    return arpLogProcess.start();
                });

        Set<String> result = logCollect.join();
        result.forEach(System.out::println);

        logs.addAll(result);
    }

    //TODO: LogEntity들 저장
    private void save(){
        Set<ManagedLog> managedLogs = logs.stream()
                .map(this::parse)
                .map(ManagedLog::create)
                .collect(Collectors.toSet());

        managedLogDAO.bulkInsert(managedLogs);
    }



    //TODO: LogDTO로 변환과정 추가
    private ArpLog parse(String log){
        String[] splited = log.split("\t");

        // 이 SSID는 프로세스가 가지고 있어야 하는 정보..?
        return new ArpLog(splited[0], splited[1], splited[2], "ECONO_5G");
    }



}
