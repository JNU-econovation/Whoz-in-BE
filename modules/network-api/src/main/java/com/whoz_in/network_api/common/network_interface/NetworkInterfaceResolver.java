package com.whoz_in.network_api.common.network_interface;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// 시스템의 현재 네트워크 인터페이스를 가져오는 역할
@Component
@RequiredArgsConstructor
public final class NetworkInterfaceResolver {
    private final ConnectionInfoResolver connectionInfoResolver;
    private final WirelessInfoResolver wirelessInfoResolver;

    public List<NetworkInterface> get() {
        Map<String, ConnectionInfo> connectionInfos = connectionInfoResolver.resolve();
        Map<String, WirelessInfo> wirelessInfos = wirelessInfoResolver.resolve();

        Set<String> interfaceNames = new HashSet<>();
        interfaceNames.addAll(connectionInfos.keySet());
        interfaceNames.addAll(wirelessInfos.keySet());

        return interfaceNames.stream()
                .map(interfaceName -> NetworkInterface.of(
                        interfaceName,
                        connectionInfos.get(interfaceName),
                        wirelessInfos.get(interfaceName)
                )).toList();
    }
}
