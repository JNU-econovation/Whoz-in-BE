package com.whoz_in.network_api.config;

import java.util.List;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

//네트워크 인터페이스와 관련된 환경 변수로부터 설정값을 읽어서 사용하기 쉽도록 가공하는 역할을 한다.
@Getter
@ConfigurationProperties(prefix = "room-setting.network-interfaces")
public class NetworkInterfaceProperties {
    private final Monitor monitor;
    private final List<Arp> arp;
    private final List<Mdns> mdns;

    public record Monitor(String interfaceName) {}
    public record Managed(List<Mdns> mdns, List<Arp> arp) {}
    public record Mdns(String ssid, String interfaceName) {}
    public record Arp(String ssid, String interfaceName) {}


    @ConstructorBinding
    public NetworkInterfaceProperties(Monitor monitor, Managed managed) {
        this.monitor = monitor;
        this.arp = managed.arp;
        this.mdns = managed.mdns;
    }
}
