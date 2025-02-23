package com.whoz_in.network_api.system_validator;

import com.whoz_in.network_api.common.NetworkInterface;
import com.whoz_in.network_api.common.process.TransientProcess;
import com.whoz_in.network_api.config.NetworkConfig;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Component
public class MonitorModeSwitcher {
    private final NetworkInterface monitorNI;
    private final String disableInterfaceCommand;
    private final String setMonitorModeCommand;
    private final String enableInterfaceCommand;

    public MonitorModeSwitcher(NetworkConfig config) {
        this.monitorNI = config.getMonitorNI();
        this.disableInterfaceCommand = "sudo -S ip link set %s down".formatted(monitorNI.getInterfaceName());
        this.setMonitorModeCommand = "sudo -S iw dev %s set type monitor".formatted(monitorNI.getInterfaceName());
        this.enableInterfaceCommand = "sudo -S ip link set %s up".formatted(monitorNI.getInterfaceName());
    }

    public void execute(){
        TransientProcess.start(disableInterfaceCommand);
        TransientProcess.start(setMonitorModeCommand);
        TransientProcess.start(enableInterfaceCommand);
    }
}
