package com.whoz_in.network_api.config;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceResolver;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*
 NetworkInterfaceResolver를 통해 시스템에 존재하는 모든 네트워크 인터페이스를 찾아낸다.
 그리고 NetworkInterfaceProperties와 명령어 설정값을 읽어와서 네트워크 인터페이스에 대한 정보를 구성한다.
*/
@Getter
@Component
public class NetworkInterfaceConfig {
    private final List<NetworkInterface> allNIs;
    private final NetworkInterface monitorNI;
    private final List<NetworkInterface> managedNIs;
    private final List<NetworkInterface> mdnsNIs;
    private final List<NetworkInterface> arpNIs;

    public NetworkInterfaceConfig(
            NetworkInterfaceProperties properties,
            NetworkInterfaceResolver networkInterfaceResolver,
            @Value("${command.monitor}") String monitorCommandTemplate,
            @Value("${command.managed.mdns}") String mdnsCommandTemplate,
            @Value("${command.managed.arp}") String arpCommandTemplate
    ) {
        List<NetworkInterface> systemNIs = networkInterfaceResolver.get();
        this.monitorNI = systemNIs.stream()
                .filter(ni -> ni.getName().equals(properties.getMonitor().interfaceName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("설정된 모니터 인터페이스가 없습니다."));
        this.arpNIs = properties.getArp().stream()
                .map(arp -> systemNIs.stream()
                        .filter(ni -> ni.getName().equals(arp.interfaceName()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("설정된 arp 인터페이스가 없습니다.")))
                .toList();
        this.mdnsNIs = properties.getMdns().stream()
                .map(mdns -> systemNIs.stream()
                        .filter(ni -> ni.getName().equals(mdns.interfaceName()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("설정된 mDNS 인터페이스가 없습니다.")))
                .toList();
        this.managedNIs = Stream.of(mdnsNIs, arpNIs)
                .flatMap(Collection::stream)
                .toList();
        this.allNIs = Stream.of(monitorNI, this.managedNIs)
                .flatMap(list -> list instanceof Collection<?> ?
                        ((Collection<NetworkInterface>) list).stream() : Stream.of((NetworkInterface) list))
                .toList();
    }
}