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
    private static final ThreadLocal<List<NetworkInterface>> cache = new ThreadLocal<>();

    private final NetworkAddressResolver networkAddressResolver;
    private final WirelessInfoResolver wirelessInfoResolver;

    // refresh()를 호출하지 않을경우 캐시된 네트워크 인터페이스 정보를 반환합니다.
    public List<NetworkInterface> get() {
        List<NetworkInterface> cached = cache.get();
        if (cached != null) return cached;
        // 캐시된게 없으면 새로 가져옴
        cache.set(fetch());
        return cache.get();
    }

    public void refresh(){
        cache.remove();
    }

    private List<NetworkInterface> fetch(){
        Map<String, NetworkAddress> connectionInfos = networkAddressResolver.resolve();
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
