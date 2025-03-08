package com.whoz_in.network_api.common.network_interface;

import com.whoz_in.network_api.config.NetworkInterfaceProfile;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 시스템의 현재 네트워크 인터페이스를 가져와서 저장 및 변경 감지
// TODO: cachedInterfaces를 SystemNetworkInterface로 분리
@Component
public final class NetworkInterfaceManager {
    private final NetworkInterfaceProfileConfig profileConfig;
    private final ApplicationEventPublisher eventPublisher;
    private final NetworkAddressResolver networkAddressResolver;
    private final WirelessInfoResolver wirelessInfoResolver;
    private Set<NetworkInterface> cachedInterfaces;

    public Set<NetworkInterface> get(){
        return cachedInterfaces;
    }

    public NetworkInterfaceManager(
            NetworkInterfaceProfileConfig profileConfig,
            ApplicationEventPublisher eventPublisher,
            NetworkAddressResolver networkAddressResolver,
            WirelessInfoResolver wirelessInfoResolver) {
        this.profileConfig = profileConfig;
        this.eventPublisher = eventPublisher;
        this.networkAddressResolver = networkAddressResolver;
        this.wirelessInfoResolver = wirelessInfoResolver;
        this.cachedInterfaces = fetch();
    }

    //주기적으로 네트워크 인터페이스 정보를 가져와서 캐시함
    //변경되면 이벤트를 발행
    @Scheduled(fixedRate = 3000)
    public void refresh() {
        Set<NetworkInterface> newInterfaces = fetch();
        checkChanged(newInterfaces);
        this.cachedInterfaces = newInterfaces;
    }

    // 이전과 새로 조회된 네트워크 인터페이스를 비교하여 변경점이 있으면 이벤트 발생
    private void checkChanged(Set<NetworkInterface> newInterfaces) {
        Map<String, NetworkInterface> preNIs = cachedInterfaces.stream()
                .collect(Collectors.toMap(NetworkInterface::getName, ni -> ni));
        Map<String, NetworkInterface> newNIs = newInterfaces.stream()
                .collect(Collectors.toMap(NetworkInterface::getName, ni -> ni));
        Set<String> oldSet = preNIs.keySet();
        Set<String> newSet = newNIs.keySet();

        // 사라진 인터페이스 처리
        Set<String> removed = new HashSet<>(oldSet);
        removed.removeAll(newSet);
        removed.forEach(niName-> eventPublisher.publishEvent(new NetworkInterfaceRemoved(niName)));
        // 생긴 인터페이스 처리
        Set<String> added = new HashSet<>(newSet);
        added.removeAll(oldSet);
        removed.forEach(niName-> eventPublisher.publishEvent(new NetworkInterfaceAdded(niName)));

        // 기존 인터페이스
        Set<String> exist = newSet.stream()
                .filter(oldSet::contains)
                .collect(Collectors.toSet());
        // 기존 인터페이스의 변화 감지
        for (String interfaceName : exist) {
            NetworkInterface oldInterface = preNIs.get(interfaceName);
            NetworkInterface newInterface = newNIs.get(interfaceName);
            if (!newInterface.isConnected()) { // 현재 연결이 끊겨있는데
                if (oldInterface.isConnected()) // 이전엔 연결되어있었으면 연결 끊김 감지
                    eventPublisher.publishEvent(new NetworkInterfaceDisconnected(newInterface.getName()));
            } else { // 현재 연결되어있는데
                if (!oldInterface.isConnected()) { // 이전엔 연결이 안되어있었을경우 다시 연결됨 감지
                    eventPublisher.publishEvent(new NetworkInterfaceReconnected(newInterface.getName()));
                } else {// 이전에도 연결되어있었을경우 (연결 유지)
                    // ip 혹은 gateway가 바뀐 경우 감지
                    if (!Objects.equals(oldInterface.getNetworkAddress(), newInterface.getNetworkAddress())) {
                        eventPublisher.publishEvent(new NetworkInterfaceAddressChanged(oldInterface, newInterface));
                    }
                }
            }

            // 무선 모드 변화 감지
            if (!Objects.equals(oldInterface.getWirelessInfo(), newInterface.getWirelessInfo())) {
                eventPublisher.publishEvent(new NetworkInterfaceModeChanged(oldInterface, newInterface));
            }
        }
    }


    // 최신 네트워크 인터페이스 정보를 반환
    private Set<NetworkInterface> fetch(){
        Map<String, NetworkAddress> connectionInfos = networkAddressResolver.resolve();
        Map<String, WirelessInfo> wirelessInfos = wirelessInfoResolver.resolve();

        //가져온 모든 인터페이스
        Set<String> fetchedInterfaces = new HashSet<>();
        fetchedInterfaces.addAll(connectionInfos.keySet());
        fetchedInterfaces.addAll(wirelessInfos.keySet());

        // 프로파일 리스트를 이름 리스트로 변경
        List<String> profileInterfaces = profileConfig.getAllProfiles().stream()
                .map(NetworkInterfaceProfile::interfaceName)
                .toList();

        //모든 인터페이스 중 프로파일로 설정된 인터페이스만 걸러냄
        return Set.copyOf(profileInterfaces.stream()
                .filter(fetchedInterfaces::contains)
                .map(interfaceName -> NetworkInterface.of(
                        interfaceName,
                        connectionInfos.get(interfaceName),
                        wirelessInfos.get(interfaceName)
                )).collect(Collectors.toSet()));
    }
}
