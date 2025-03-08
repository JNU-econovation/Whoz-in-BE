package com.whoz_in.network_api.config;

import java.util.List;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

// NetworkInterfaceProfile을 구성하기 위한 값들을 환경 변수로부터 읽는다.
// 그리고 Profile을 구성하는 NetworkInterfaceProfileConfig가 사용하기 쉽도록 가공해놓는다.
@Getter
@ConfigurationProperties(prefix = "room-setting.network-interfaces")
public class NetworkInterfaceProfileProperties {
    private final Monitor monitor;
    private final List<Arp> arp;
    private final List<Mdns> mdns;

    public record Monitor(String interfaceName) {}
    public record Managed(List<Mdns> mdns, List<Arp> arp) {}
    public record Mdns(String ssid, String interfaceName) {}
    public record Arp(String ssid, String interfaceName) {}


    @ConstructorBinding
    public NetworkInterfaceProfileProperties(Monitor monitor, Managed managed) {
        this.monitor = monitor;
        this.arp = managed.arp;
        this.mdns = managed.mdns;
    }
}
