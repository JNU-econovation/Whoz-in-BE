package com.whoz_in.network_api.config;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceProfile;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceResolver;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// 시스템의 네트워크 인터페이스를 resolver로 불러온다.
// 그리고 환경변수에 설정된 네트워크 인터페이스의 정보들을 profile로 매핑한다.
@Getter
@Component
public class NetworkInterfaceProfileConfig {
    private final List<NetworkInterfaceProfile> allProfiles;
    private final List<NetworkInterfaceProfile> managedProfiles;
    private final NetworkInterfaceProfile monitorProfile;
    private final List<NetworkInterfaceProfile> arpProfiles;
    private final List<NetworkInterfaceProfile> mdnsProfiles;

    public NetworkInterfaceProfileConfig(
            NetworkInterfaceResolver resolver,
            NetworkInterfaceProfileProperties properties,
            @Value("${command.monitor}") String monitorCommandTemplate,
            @Value("${command.managed.mdns}") String mdnsCommandTemplate,
            @Value("${command.managed.arp}") String arpCommandTemplate
    ) {
        // 인터페이스 이름으로 찾을 수 있도록 맵으로 변경
        Map<String, NetworkInterface> niMap = resolver.get().stream()
                .collect(Collectors.toMap(NetworkInterface::getName, Function.identity()));

        String monitorInterfaceName = properties.getMonitor().interfaceName();
        NetworkInterface monitorInterface = niMap.get(monitorInterfaceName);
        if (monitorInterface == null) {
            throw new IllegalStateException("설정된 모니터 인터페이스가 없습니다.");
        }
        this.monitorProfile = new NetworkInterfaceProfile(
                monitorInterface,
                generateCommand(monitorCommandTemplate, monitorInterface.getName()),
                null
        );

        this.arpProfiles = properties.getArp().stream()
                .map(arpProp -> {
                    NetworkInterface arpNI = niMap.get(arpProp.interfaceName());
                    if (arpNI == null) {
                        throw new IllegalStateException("설정된 arp 인터페이스가 없습니다.");
                    }
                    return new NetworkInterfaceProfile(
                            arpNI,
                            generateCommand(arpCommandTemplate, arpNI.getName()),
                            arpProp.ssid()
                    );
                })
                .toList();

        this.mdnsProfiles = properties.getMdns().stream()
                .map(mdnsProp -> {
                    NetworkInterface mdnsNI = niMap.get(mdnsProp.interfaceName());
                    if (mdnsNI == null){
                        throw  new IllegalStateException("설정된 mdns 인터페이스가 없습니다.");
                    }
                    return new NetworkInterfaceProfile(
                            mdnsNI,
                            generateCommand(mdnsCommandTemplate, mdnsNI.getName()),
                            mdnsProp.ssid()
                    );
                })
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
