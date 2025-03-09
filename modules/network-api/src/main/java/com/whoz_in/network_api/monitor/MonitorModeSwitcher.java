package com.whoz_in.network_api.monitor;

import com.whoz_in.network_api.common.network_interface.NetworkInterfaceModeChanged;
import com.whoz_in.network_api.common.process.TransientProcess;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("prod")
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

    private void execute(){
        log.info("{}를 모니터 모드로 전환합니다..", this.interfaceName);
        TransientProcess.create(disableInterfaceCommand).waitTermination();
        TransientProcess.create(setMonitorModeCommand).waitTermination();
        TransientProcess.create(enableInterfaceCommand).waitTermination();
    }

    @EventListener
    private void handleNiModeChanged(NetworkInterfaceModeChanged event) {
        // 모니터 모드 인터페이스이고 모니터 모드가 아닐 때
        if (this.interfaceName.equals(event.getInterfaceName()) && !event.getNow().mode().equals("monitor")) {
            log.info("{}의 모드가 monitor가 아닙니다. (이전: {}, 현재: {}). 모니터 모드로 전환합니다.",
                    event.getInterfaceName(), event.getPre().mode(), event.getNow().mode());
            execute();
        }
    }
}
