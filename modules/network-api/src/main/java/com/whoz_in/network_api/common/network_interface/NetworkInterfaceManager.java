package com.whoz_in.network_api.common.network_interface;

import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status.ADDED;
import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status.ADDED_AND_RECONNECTED;
import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status.DISCONNECTED;
import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status.MODE_CHANGED;
import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status.RECONNECTED;
import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status.REMOVED;

import com.whoz_in.network_api.config.NetworkInterfaceProfile;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 시스템의 현재 네트워크 인터페이스를 가져와서 저장 및 변경 감지
// TODO: cachedInterfaces를 SystemNetworkInterface로 분리
@Slf4j
@Component
public final class NetworkInterfaceManager {
    private final NetworkInterfaceProfileConfig profileConfig;
    private final ApplicationEventPublisher eventPublisher;
    private final NetworkAddressResolver networkAddressResolver;
    private final WirelessInfoResolver wirelessInfoResolver;
    private Map<String, NetworkInterface> cachedInterfaces;

    public Map<String, NetworkInterface> get(){
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
        logInterfaces();
    }

    public void logInterfaces(){
        log.info("네트워크 인터페이스 목록\n{}", formattedNIs());
    }
    private String formattedNIs(){
        return this.cachedInterfaces.values().stream()
                .map(ni-> "%s\n%s\n%s\n%s\n%s".formatted(
                                "name: " + ni.getName(),
                                "is_connected: "+ ni.isConnected(),
                                "is_wireless: " + ni.isWireless(),
                                "address: " + ni.getNetworkAddress(),
                                "wireless_info: " + ni.getWirelessInfo()
                        )
                ).collect(Collectors.joining("\n====================================\n"));
    }

    //주기적으로 네트워크 인터페이스 정보를 가져와서 캐시함
    //변경되면 이벤트를 발행
    @Scheduled(fixedRate = 3000)
    public void refresh() {
        Map<String, NetworkInterface> newInterfaces = fetch();
        checkChanged(newInterfaces);
        this.cachedInterfaces = newInterfaces;
    }

    // 이전과 새로 조회된 네트워크 인터페이스를 비교하여 변경점이 있으면 이벤트 발생
    private void checkChanged(Map<String, NetworkInterface> nowInterfaces) {
        Set<String> oldSet = new HashSet<>(cachedInterfaces.keySet());
        Set<String> nowSet = new HashSet<>(nowInterfaces.keySet());

        // 제거된 인터페이스: 기존에는 있었지만 현재는 없는 경우
        Set<String> removed = new HashSet<>(oldSet);
        removed.removeAll(nowSet);
        for (String niName : removed) {
            NetworkInterface oldInterface = cachedInterfaces.get(niName);
            log.error("인터페이스 {}가 사라졌습니다.", niName);
            eventPublisher.publishEvent(
                    new NetworkInterfaceStatusEvent(niName, oldInterface, null, REMOVED)
            );
        }

        // 새로 추가된 인터페이스: 현재에는 있으나 기존에는 없는 경우
        Set<String> added = new HashSet<>(nowSet);
        added.removeAll(oldSet);
        for (String niName : added) {
            NetworkInterface nowInterface = nowInterfaces.get(niName);
            if (nowInterface.isConnected()) {
                log.info("{}가 추가되고 연결되었습니다.", niName);
                eventPublisher.publishEvent(
                        new NetworkInterfaceStatusEvent(niName, null, nowInterface, ADDED_AND_RECONNECTED)
                );
            } else {
                log.info("{}가 새로 추가되었습니다.", niName);
                eventPublisher.publishEvent(
                        new NetworkInterfaceStatusEvent(niName, null, nowInterface, ADDED)
                );
            }
        }

        // 기존에 존재하는 인터페이스 (both old and now)
        Set<String> existing = new HashSet<>(nowSet);
        existing.retainAll(oldSet);
        for (String niName : existing) {
            NetworkInterface oldInterface = cachedInterfaces.get(niName);
            NetworkInterface nowInterface = nowInterfaces.get(niName);

            // 연결 상태 변화
            if (oldInterface.isConnected() && !nowInterface.isConnected()) {
                log.error("{}의 네트워크 연결이 끊겼습니다.", niName);
                eventPublisher.publishEvent(
                        new NetworkInterfaceStatusEvent(niName, oldInterface, nowInterface, DISCONNECTED)
                );
            } else if (!oldInterface.isConnected() && nowInterface.isConnected()) {
                log.info("{}가 다시 네트워크에 연결되었습니다.", niName);
                eventPublisher.publishEvent(
                        new NetworkInterfaceStatusEvent(niName, oldInterface, nowInterface, RECONNECTED)
                );
            } else if (oldInterface.isConnected()) {
                // 연결 유지 중인데 IP 혹은 gateway가 변경된 경우 (fetch 주기(3초) 안에 연결이 끊겼다가 다시 연결되어야 해서 일어날 수 없을듯)
                if (!Objects.equals(oldInterface.getNetworkAddress(), nowInterface.getNetworkAddress())) {
                    log.error("{}의 아이피가 변경되었습니다.", niName);
                }
            }

            // 무선 모드 변화 감지 (연결 상태와 상관없이)
            if (!Objects.equals(oldInterface.getWirelessInfo(), nowInterface.getWirelessInfo())) {
                log.error("{}의 무선 모드가 변경되었습니다. {} -> {}",
                        niName,
                        oldInterface.getWirelessInfo(),
                        nowInterface.getWirelessInfo());
                eventPublisher.publishEvent(
                        new NetworkInterfaceStatusEvent(niName, oldInterface, nowInterface, MODE_CHANGED)
                );
            }
        }
    }


    // 최신 네트워크 인터페이스 정보를 반환
    private Map<String, NetworkInterface> fetch(){
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
        return profileInterfaces.stream()
                .filter(fetchedInterfaces::contains)
                .collect(Collectors.toMap(
                        interfaceName -> interfaceName,
                        interfaceName -> NetworkInterface.of(
                                interfaceName,
                                connectionInfos.get(interfaceName),
                                wirelessInfos.get(interfaceName)
                        ),
                        (existing, replacement) -> existing  // 중복 시 기존 값 유지
                ));
    }
}
