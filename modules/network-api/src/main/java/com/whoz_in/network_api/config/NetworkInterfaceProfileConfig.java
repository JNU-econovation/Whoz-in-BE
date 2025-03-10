package com.whoz_in.network_api.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// 환경변수에 설정된 네트워크 인터페이스의 정보들을 profile로 매핑한다.
@Getter
@Component
public class NetworkInterfaceProfileConfig {
    private final List<NetworkInterfaceProfile> allProfiles;
    private final List<NetworkInterfaceProfile> managedProfiles;
    private final NetworkInterfaceProfile monitorProfile;
    private final List<NetworkInterfaceProfile> arpProfiles;
    private final List<NetworkInterfaceProfile> mdnsProfiles;

    public NetworkInterfaceProfile getBySsid(String ssid){
        return allProfiles.stream()
                .filter(profile -> profile.ssid() != null) // managed인 프로파일만
                .filter(profile -> profile.ssid().equals(ssid))
                .findAny()
                .orElseThrow(()->new IllegalStateException(ssid + "는 설정에 존재하지 않는 ssid"));
    }

    public NetworkInterfaceProfileConfig(
            NetworkInterfaceProfileProperties properties,
            @Value("${command.monitor}") String monitorCommandTemplate,
            @Value("${command.managed.mdns}") String mdnsCommandTemplate,
            @Value("${command.managed.arp}") String arpCommandTemplate
    ) {
        this.monitorProfile = new NetworkInterfaceProfile(
                properties.getMonitor().interfaceName(),
                generateCommand(monitorCommandTemplate, properties.getMonitor().interfaceName()),
                null
        );

        this.arpProfiles = properties.getArp().stream()
                .map(arpProp -> new NetworkInterfaceProfile(
                        arpProp.interfaceName(),
                        generateCommand(arpCommandTemplate, arpProp.interfaceName()),
                        arpProp.ssid()
                ))
                .toList();

        this.mdnsProfiles = properties.getMdns().stream()
                .map(mdnsProp -> new NetworkInterfaceProfile(
                        mdnsProp.interfaceName(),
                        generateCommand(mdnsCommandTemplate, mdnsProp.interfaceName()),
                        mdnsProp.ssid()
                ))
                .toList();

        this.managedProfiles = Stream.of(mdnsProfiles, arpProfiles)
                .flatMap(Collection::stream)
                .toList();
        this.allProfiles = Stream.of(monitorProfile, this.managedProfiles)
                .flatMap(list -> list instanceof Collection<?> ?
                        ((Collection<NetworkInterfaceProfile>) list).stream() : Stream.of((NetworkInterfaceProfile) list))
                .toList();
    }

    private String generateCommand(String commandTemplate, String interfaceName) {
        return commandTemplate.replace("{{interface}}", interfaceName);
    }
}
