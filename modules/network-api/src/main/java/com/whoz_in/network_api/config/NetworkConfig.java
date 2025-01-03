package com.whoz_in.network_api.config;

import com.whoz_in.network_api.common.NetworkInterface;
import com.whoz_in.network_api.managed.ManagedInfo;
import com.whoz_in.network_api.monitor.MonitorInfo;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;

//실행시킬 프로세스들이 필요로 하는 정보를 제공하는 역할을 한다.
//다른 모듈이 구현한 LogWriterConfig를 통해 정보를 초기화한다.
@Getter
@Component
public class NetworkConfig {
    private final String room;
    private final String sudoPassword;
    private final List<NetworkInterface> networkInterfaces;
    private final MonitorInfo monitorInfo;
    private final List<ManagedInfo> mdnsList;
    private final List<ManagedInfo> arpList;

    public NetworkConfig(
            NetworkConfigProperties properties,
            @Value("${sudo_password}") String sudoPassword,
            @Value("${command.monitor}") String monitorCommandTemplate,
            @Value("${command.managed.mdns}") String mdnsCommandTemplate,
            @Value("${command.managed.arp}") String arpCommandTemplate
    ) {
        this.room = properties.getRoom();
        this.sudoPassword = sudoPassword;
        this.networkInterfaces = properties.getNIs();
        this.monitorInfo = new MonitorInfo(properties.getMonitorNI(), generateCommand(monitorCommandTemplate, properties.getMonitorNI().getName()));
        this.mdnsList=properties.getMdnsNIs().stream()
                .map(ni-> new ManagedInfo(ni, generateCommand(mdnsCommandTemplate, ni.getName())))
                .toList();
        this.arpList=properties.getArpNIs().stream()
                .map(ni-> new ManagedInfo(ni, generateCommand(arpCommandTemplate, ni.getName())))
                .toList();
    }

    private String generateCommand(String commandTemplate, String interfaceName) {
        String replace = commandTemplate.replace("{{interface}}", interfaceName);
        System.out.println("replace = " + replace);
        return replace;
    }


    @Getter
    @ConfigurationProperties(prefix = "log-writer-setting")
    public static class NetworkConfigProperties {

        private final String room;
        private final NetworkInterface monitorNI;
        private final List<NetworkInterface> mdnsNIs;
        private final List<NetworkInterface> arpNIs;

        public record Monitor(String interfaceName) {}
        public record Managed(List<Mdns> mdns, List<Arp> arp) {
            public record Mdns(String ssid, String interfaceName) {}
            public record Arp(String name, String ssid, String interfaceName) {}
        }

        public List<NetworkInterface> getNIs() {
            List<NetworkInterface> nis = new ArrayList<>();
            nis.add(getMonitorNI());
            nis.addAll(getMdnsNIs());
            nis.addAll(getArpNIs());
            return nis;
        }

        @ConstructorBinding
        public NetworkConfigProperties(
                String roomName,
                Monitor monitor,
                Managed managed
        ) {
            this.room = roomName;
            this.monitorNI = new NetworkInterface(monitor.interfaceName, null, "monitor");
            this.mdnsNIs = managed.mdns.stream()
                    .map(ni -> new NetworkInterface(ni.interfaceName, ni.ssid, "managed"))
                    .toList();
            this.arpNIs = managed.arp.stream()
                    .map(ni -> new NetworkInterface(ni.interfaceName, ni.ssid, "managed"))
                    .toList();
        }
    }
}