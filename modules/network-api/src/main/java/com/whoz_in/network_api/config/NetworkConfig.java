package com.whoz_in.network_api.config;

import com.whoz_in.network_api.common.NetworkInterface;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//이 서버의 전체적인 설정 값을 관리한다.
@Getter
@Component
public class NetworkConfig {
    private final String room;
    private final String sudoPassword;
    private final List<NetworkInterface> networkInterfaces;
    private final NetworkInterface monitorNI;
    private final List<NetworkInterface> mdnsNIs;
    private final List<NetworkInterface> arpNIs;

    public NetworkConfig(
            NetworkInterfaceProperties properties,
            @Value("${room-setting.room-name}") String roomName,
            @Value("${sudo_password}") String sudoPassword,
            @Value("${command.monitor}") String monitorCommandTemplate,
            @Value("${command.managed.mdns}") String mdnsCommandTemplate,
            @Value("${command.managed.arp}") String arpCommandTemplate
    ) {
        this.room = roomName;
        this.sudoPassword = sudoPassword;
        this.monitorNI = new NetworkInterface(properties.getMonitor().interfaceName(), null, "monitor",
                null, generateCommand(monitorCommandTemplate, properties.getMonitor().interfaceName()));
        this.mdnsNIs = properties.getMdns().stream()
                .map(mdns -> {
                    String altSsid = (mdns.altSsid() != null) ? mdns.altSsid() : mdns.realSsid();
                    return new NetworkInterface(mdns.interfaceName() , mdns.realSsid(),  "managed", altSsid,
                            generateCommand(mdnsCommandTemplate, mdns.interfaceName()));
                })
                .toList();
        this.arpNIs = properties.getArp().stream()
                .map(arp -> {
                    String altSsid = (arp.altSsid() != null) ? arp.altSsid() : arp.realSsid();
                    return new NetworkInterface(arp.interfaceName(), arp.realSsid(), "managed", altSsid,
                            generateCommand(arpCommandTemplate, arp.interfaceName()));
                })
                .toList();
        this.networkInterfaces = Stream.of(monitorNI, mdnsNIs, arpNIs)
                .flatMap(list -> list instanceof Collection<?> ?
                        ((Collection<NetworkInterface>) list).stream() : Stream.of((NetworkInterface) list))
                .toList();
    }

    private static String generateCommand(String commandTemplate, String interfaceName) {
        return commandTemplate.replace("{{interface}}", interfaceName);
    }
}