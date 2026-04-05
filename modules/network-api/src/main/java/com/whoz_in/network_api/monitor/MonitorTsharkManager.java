package com.whoz_in.network_api.monitor;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import com.whoz_in.network_api.common.process.ContinuousProcess;
import com.whoz_in.network_api.common.process.ResilientContinuousProcess;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus.ADDED;
import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus.MODE_CHANGED;
import static com.whoz_in.network_api.common.network_interface.WirelessMode.MONITOR;

@Slf4j
@Component
public class MonitorTsharkManager {
    private final String interfaceName;
    private final NetworkInterfaceManager networkInterfaceManager;
    @Getter
    private final ContinuousProcess process;

    public MonitorTsharkManager(NetworkInterfaceProfileConfig config, NetworkInterfaceManager networkInterfaceManager) {
        this.interfaceName = config.getMonitorProfile().interfaceName();
        this.networkInterfaceManager = networkInterfaceManager;
        this.process = ResilientContinuousProcess.create(config.getMonitorProfile().command());
    }

    // 모니터 모드로 바꼈을 경우 다시 실행
    @EventListener
    private void handle(NetworkInterfaceStatusEvent event) {
        if (!interfaceName.equals(event.interfaceName())) {
            return;
        }
        if (event.status() != ADDED && event.status() != MODE_CHANGED) {
            return;
        }

        NetworkInterface networkInterface = networkInterfaceManager.get().get(interfaceName);
        if (networkInterface == null || !networkInterface.isWireless()) {
            return;
        }
        if (networkInterface.getWirelessInfo().mode() != MONITOR) {
            return;
        }

        process.restart();
        log.info("[monitor] tshark가 재실행되었습니다.");
    }

    // 오랫동안 켜진 tshark는 패킷을 제대로 잡지 못하는것으로 확인되어 오전 6시에 재실행한다.
    @Scheduled(cron = "0 0 6 * * *")
    private void restartTshark() {
        process.restart();
        log.info("[monitor] tshark가 재실행되었습니다.");
    }
}
