package com.whoz_in.network_api.managed.mdns;

import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus.ADDED_AND_RECONNECTED;
import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus.RECONNECTED;

import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import com.whoz_in.network_api.common.process.ResilientContinuousProcess;
import com.whoz_in.network_api.config.NetworkInterfaceProfile;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MdnsTsharkManager {
    private final Map<String, ResilientContinuousProcess> processesByInterfaceName;
    @Getter
    private final Map<NetworkInterfaceProfile, ResilientContinuousProcess> processes;

    public MdnsTsharkManager(NetworkInterfaceProfileConfig config) {
        processes = Collections.unmodifiableMap(
                config.getMdnsProfiles().stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                profile -> ResilientContinuousProcess.create(profile.command())
                        ))
        );
        processesByInterfaceName = processes.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().interfaceName(),
                        Map.Entry::getValue
                ));
    }

    // 다시 연결됐을경우 재실행
    @EventListener
    private void handle(NetworkInterfaceStatusEvent event) {
        if (event.status() != RECONNECTED && event.status() != ADDED_AND_RECONNECTED) {
            return;
        }

        ResilientContinuousProcess process = processesByInterfaceName.get(event.interfaceName());
        if (process == null) {
            return;
        }

        process.restart();
        log.info("[managed - mdns({})] tshark가 재실행되었습니다.", event.interfaceName());
    }

    // 오랫동안 켜진 tshark는 모든 대역의 mdns 패킷을 받지 못하는 것으로 확인되어 오전 9시, 오후 9시에 재실행한다.
    @Scheduled(cron = "0 0 9,21 * * *")
    private void restartTshark() {
        processes.forEach((profile, process) -> {
            process.restart();
            log.info("[managed - mdns({})] tshark가 재실행되었습니다.", profile.ssid());
        });
    }
}
