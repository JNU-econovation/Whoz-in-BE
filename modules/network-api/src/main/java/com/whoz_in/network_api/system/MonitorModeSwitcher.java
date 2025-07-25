package com.whoz_in.network_api.system;

import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus.ADDED;
import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus.MODE_CHANGED;
import static com.whoz_in.network_api.common.network_interface.WirelessMode.MONITOR;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import com.whoz_in.network_api.common.LinuxCondition;
import com.whoz_in.network_api.common.process.TransientProcess;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// 모니터 모드여야 하는 인터페이스를 모니터 모드로 설정함
@Slf4j
@Conditional(LinuxCondition.class)
@Component
public class MonitorModeSwitcher {
    private final String interfaceName;
    private final String disableInterfaceCommand;
    private final String setMonitorModeCommand;
    private final String enableInterfaceCommand;

    public MonitorModeSwitcher(NetworkInterfaceProfileConfig config) {
        this.interfaceName = config.getMonitorProfile().interfaceName();
        this.disableInterfaceCommand = "sudo -S ip link set %s down".formatted(interfaceName);
        this.setMonitorModeCommand = "sudo -S iw dev %s set type monitor".formatted(interfaceName);
        this.enableInterfaceCommand = "sudo -S ip link set %s up".formatted(interfaceName);
    }

    // 이미 모니터 모드였는지 확인 안함
    public void switchToMonitor(){
        log.info("{}를 모니터 모드로 전환합니다..", this.interfaceName);
        TransientProcess.create(disableInterfaceCommand).waitForTermination();
        TransientProcess.create(setMonitorModeCommand).waitForTermination();
        TransientProcess.create(enableInterfaceCommand).waitForTermination();
    }

    @EventListener
    private void handle(NetworkInterfaceStatusEvent event) {
        // 모드가 바꼈거나 새로 추가됐을때
        if (event.status() == MODE_CHANGED || event.status() == ADDED) {
            NetworkInterface now = event.now();
            // 모니터 모드 인터페이스이고 모니터 모드가 아닐 때
            if (this.interfaceName.equals(now.getName()) && now.getWirelessInfo().mode() != MONITOR){
                log.info("{}의 모드가 현재 {}입니다. 모니터 모드로 전환합니다.",
                        now.getName(), now.getWirelessInfo().mode());
                switchToMonitor();
            }
        }
    }
}
