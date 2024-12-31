package com.whoz_in.network_api.config;

import com.whoz_in.log_writer.common.NetworkInterface;
import com.whoz_in.log_writer.config.LogWriterConfig;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "log-writer-setting")
public class LogWriterConfigImpl implements LogWriterConfig {
    private final String room;
    private final NetworkInterface monitorNI;
    private final List<NetworkInterface> mdnsNIs;
    private final List<NetworkInterface> arpNIs;

    public record Monitor(String interfaceName){}
    public record Managed(List<Mdns> mdns, List<Arp> arp) {
        public record Mdns(String ssid, String interfaceName) {}
        public record Arp(String name, String ssid, String interfaceName) {}
    }

    @Override
    public String getRoomName() {
        return this.room;
    }

    @Override
    public NetworkInterface getMonitorNI() {
        return this.monitorNI;
    }

    @Override
    public List<NetworkInterface> getArpNIs() {
        return this.arpNIs;
    }

    @Override
    public List<NetworkInterface> getMdnsNIs() {
        return this.mdnsNIs;
    }

    @ConstructorBinding
    public LogWriterConfigImpl(
            String roomName,
            Monitor monitor,
            Managed managed
    ) {
        this.room = roomName;
        this.monitorNI = new NetworkInterface(monitor.interfaceName, null, "monitor");
        this.mdnsNIs = managed.mdns.stream()
                .map(ni->new NetworkInterface(ni.interfaceName, ni.ssid, "managed"))
                .toList();
        this.arpNIs = managed.arp.stream()
                .map(ni->new NetworkInterface(ni.interfaceName, ni.ssid, "managed"))
                .toList();
    }
}

