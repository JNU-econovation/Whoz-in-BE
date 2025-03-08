package com.whoz_in.network_api.managed.mdns;

import com.whoz_in.domain.network_log.ManagedLog;
import com.whoz_in.domain.network_log.ManagedLogRepository;
import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.config.NetworkInterfaceProfile;
import com.whoz_in.network_api.common.process.ContinuousProcess;
import com.whoz_in.network_api.common.process.ResilientContinuousProcess;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import com.whoz_in.network_api.managed.ParsedLog;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//TODO: tshark가 종료되지 않더라도 오류가 발생할 수 있으려나? 이 경우에도 로깅이 필요하긴 함
@Slf4j
@Component
public class MdnsLogWriter {
    private final String room;
    private final Map<NetworkInterfaceProfile, ResilientContinuousProcess> processes;
    private final MdnsLogParser parser;
    private final ManagedLogRepository repository;

    public MdnsLogWriter(@Value("${room-setting.room-name}") String room, NetworkInterfaceProfileConfig config, ManagedLogRepository repository, MdnsLogParser parser) {
        this.room = room;
        this.repository = repository;
        this.parser = parser;
        this.processes = config.getMdnsProfiles().stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                profile -> ResilientContinuousProcess.create(profile.command()))
                        );
    }

    //주기적으로 로그를 저장함
    @Scheduled(initialDelay = 10000, fixedDelay = 5000)
    private void saveLogs() {
        List<ManagedLog> totalLogs = this.processes.entrySet().parallelStream()
                .filter(niProc -> niProc.getValue().isAlive())
                .map(niProc -> getLogsFromProcess(niProc.getKey(), niProc.getValue()))
                .flatMap(Collection::stream)
                .toList();
        repository.saveAll(totalLogs);
    }

    private List<ManagedLog> getLogsFromProcess(NetworkInterfaceProfile profile, ContinuousProcess process) {
        Set<ParsedLog> logs = process.readLines().stream()
                .map(parser::parse)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        log.info("[managed - mdns({})] log to save : {}", profile.ssid(), logs.size());

        //Parsed를 Managed로 변환
        return logs.stream()
                .map(log -> new ManagedLog(
                        log.getMac(), log.getIp(),
                        log.getDeviceName(), profile.ssid(), room,
                        log.getCreatedAt(), log.getCreatedAt()
                ))
                .toList();
    }
}
