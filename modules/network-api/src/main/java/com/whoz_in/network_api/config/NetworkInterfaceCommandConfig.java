package com.whoz_in.network_api.config;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceCommand;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NetworkInterfaceCommandConfig {
    private final NetworkInterfaceCommand monitorCommand;
    private final List<NetworkInterfaceCommand> arpCommands;
    private final List<NetworkInterfaceCommand> mdnsCommands;

    public NetworkInterfaceCommandConfig(
            NetworkInterfaceConfig config,
            @Value("${command.monitor}") String monitorCommandTemplate,
            @Value("${command.managed.mdns}") String mdnsCommandTemplate,
            @Value("${command.managed.arp}") String arpCommandTemplate) {
        NetworkInterface monitorNI = config.getMonitorNI();
        this.monitorCommand = new NetworkInterfaceCommand(
                monitorNI,
                generateCommand(monitorCommandTemplate, monitorNI.getName())
        );
        this.mdnsCommands = config.getMdnsNIs().stream()
                .map(ni -> new NetworkInterfaceCommand(
                        ni,
                        generateCommand(mdnsCommandTemplate, ni.getName())
                )).toList();
        this.arpCommands = config.getArpNIs().stream()
                .map(ni -> new NetworkInterfaceCommand(
                        ni,
                        generateCommand(arpCommandTemplate, ni.getName())
                )).toList();
    }

    private String generateCommand(String commandTemplate, String interfaceName) {
        return commandTemplate.replace("{{interface}}", interfaceName);
    }
}
