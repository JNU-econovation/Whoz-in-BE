package com.whoz_in.network_api.managed.arp;


import com.whoz_in.domain.network_log.ManagedLog;
import com.whoz_in.domain.network_log.ManagedLogRepository;
import com.whoz_in.network_api.common.network_interface.NetworkInterface;

import com.whoz_in.network_api.common.network_interface.NetworkInterfaceCommand;
import com.whoz_in.network_api.common.process.TransientProcess;
import com.whoz_in.network_api.config.NetworkInterfaceCommandConfig;
import com.whoz_in.network_api.managed.ParsedLog;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ArpLogWriter {
    private final String room;
    private final ManagedLogRepository repository;
    private final ArpLogParser parser;
    private final List<NetworkInterfaceCommand> arpNIs;

    public ArpLogWriter(
            @Value("${room-setting.room-name}") String room,
            ManagedLogRepository repository,
            ArpLogParser parser,
            NetworkInterfaceCommandConfig config
    ) {
        this.room = room;
        this.repository = repository;
        this.parser = parser;
        this.arpNIs = config.getArpCommands();
    }

    //주기적으로 arp 명령어를 실행하여 로그를 저장함
    @Scheduled(initialDelay = 10000, fixedDelay = 5000)
    private void scan() {
        List<ManagedLog> logs = arpNIs.stream()
                .collect(Collectors.toMap(
                        ni -> ni,
                        ni -> TransientProcess.create(ni.command())
                )).entrySet().stream()
                .map(entry -> getLogsFromProcess(entry.getKey().ni(), entry.getValue()))
                .flatMap(Collection::stream)
                .toList();
        repository.saveAll(logs);
    }

    //프로세스의 출력들을 로그로 변환한다.
    private List<ManagedLog> getLogsFromProcess(NetworkInterface ni, TransientProcess process){
        Set<ParsedLog> logs = process.results().stream()
                .filter(parser::validate)
                .map(parser::parse)
                .collect(Collectors.toSet());//Set으로 중복 제거

        String ssid = ni.getWirelessInfo().ssid();
        log.info("[managed - arp({})] log to save : {}", ssid, logs.size());
        //parsedLog를 저장할 수 있는 ManagedLog로 변환
        return logs.stream()
                .map(log -> new ManagedLog(log.getMac(), log.getIp(), log.getDeviceName(), ssid, room,
                        log.getCreatedAt(), log.getCreatedAt()))
                .toList();
    }
}
