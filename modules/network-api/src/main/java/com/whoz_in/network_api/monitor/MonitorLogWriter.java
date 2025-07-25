package com.whoz_in.network_api.monitor;

import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.network_api.common.process.ResilientContinuousProcess;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
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

    public MonitorLogWriter(@Value("${room-name}") String room, MonitorLogParser parser, MonitorLogRepository repository, NetworkInterfaceProfileConfig config) {
        this.parser = parser;
        this.repository = repository;
        this.room = room;
        this.process = ResilientContinuousProcess.create(config.getMonitorProfile().command());
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

    // TODO: 모니터 모드로 변경됨 이벤트 받으면 재실행

    // 오랫동안 켜진 tshark는 패킷을 제대로 잡지 못하는것으로 확인되어 오전 6시에 재실행한다.
    @Scheduled(cron = "0 0 6 * * *")
    private void refreshTshark(){
        if (!this.process.isAlive()) return;
        this.process.restart();
        log.info("[monitor] tshark가 재실행되었습니다.");
    }
}
