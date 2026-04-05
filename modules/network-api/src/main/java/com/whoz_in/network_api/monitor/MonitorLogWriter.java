package com.whoz_in.network_api.monitor;

import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.network_api.common.process.ResilientContinuousProcess;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MonitorLogWriter {
    private final ResilientContinuousProcess process;
    private final String room;
    private final MonitorLogParser parser;
    private final MonitorLogRepository  repository;

    public MonitorLogWriter(
            @Value("${room-name}") String room,
            MonitorLogParser parser,
            MonitorLogRepository repository,
            MonitorTsharkManager tsharkManager) {
        this.parser = parser;
        this.repository = repository;
        this.room = room;
        this.process = tsharkManager.getProcess();
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 3000)
    private void saveLogs(){
        //프로세스 죽었으면 기록 안함
        if (!process.isAlive()) return;

        Set<String> macs = new HashSet<>();
        process.readLines().stream()
                .map(parser::parse)
                .forEach(macs::addAll);
        macs.remove("");
        log.info("[monitor] mac to save: " + macs.size());
        repository.saveAll(macs.stream().map(mac -> new MonitorLog(mac, room)).toList());
    }
}
