package com.whoz_in.network_api.common.network_interface;

import com.whoz_in.network_api.config.NetworkInterfaceProfile;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus.*;
import static com.whoz_in.network_api.common.network_interface.WirelessMode.MANAGED;

// 시스템의 현재 네트워크 인터페이스를 주기적으로 가져와서 저장 및 변경 감지
@Slf4j
@Component
public final class NetworkInterfaceManager {
    private final ApplicationEventPublisher eventPublisher;
    private final NetworkAddressResolver networkAddressResolver;
    private final WirelessInfoResolver wirelessInfoResolver;
    // TODO: VO로 만들기
    private Map<String, NetworkInterface> nowInterfaces;
    private final Map<String, NetworkInterfaceStatus> unavailableInterfaces;

    // 아래 profile들은 NetworkInterfaceProfileConfig에서 얻을 수 있지만, 자주 쓰는 것들이니 파싱해둠
    private final List<String> allProfiles;
    private final List<String> managedProfiles; // 존재해야 하는 managed 인터페이스
    private final String monitorProfile; // 존재해야 하는 monitor 인터페이스

    public Map<String, NetworkInterface> get() {
        return nowInterfaces;
    }

    public NetworkInterfaceManager(
        NetworkInterfaceProfileConfig profileConfig,
        ApplicationEventPublisher eventPublisher,
        NetworkAddressResolver networkAddressResolver,
        WirelessInfoResolver wirelessInfoResolver) {
        this.allProfiles =  profileConfig.getAllProfiles().stream()
                .map(NetworkInterfaceProfile::interfaceName)
                .toList();
        this.managedProfiles = profileConfig.getManagedProfiles().stream()
                .map(NetworkInterfaceProfile::interfaceName).toList();
        this.monitorProfile = profileConfig.getMonitorProfile().interfaceName();
        this.eventPublisher = eventPublisher;
        this.networkAddressResolver = networkAddressResolver;
        this.wirelessInfoResolver = wirelessInfoResolver;
        this.nowInterfaces = fetch();
        this.unavailableInterfaces = new HashMap<>();
        logInterfaces();
    }
    public NetworkInterface getByName(String interfaceName){
        NetworkInterface ni = this.nowInterfaces.get(interfaceName);
        if (ni == null) throw new IllegalStateException(interfaceName + "라는 인터페이스는 없음");
        return ni;
    };

    // isAvailable은 네트워크 인터페이스가 잘 동작하는지를 판단하는 메서드
    // TODO: 이 클래스는 네트워크 인터페이스를 나타내는 것이 아니라 관리라서 여기에 있으면 안됨
    public boolean isAvailable(){
        return unavailableInterfaces.isEmpty();
    }
    public boolean isAvailable(WirelessMode mode){
        if (mode == MANAGED){
            return unavailableInterfaces.keySet().stream().noneMatch(managedProfiles::contains);
        } else {
            return !unavailableInterfaces.containsKey(monitorProfile);
        }
    }

    // TODO: 이 클래스는 네트워크 인터페이스를 나타내는 것이 아니라 관리라서 여기에 있으면 안됨
    public void logInterfaces(){
        log.info("네트워크 인터페이스 목록\n{}", this);
    }
    @Override
    public String toString(){
        return this.nowInterfaces.values().stream()
                .map(ni-> "%s\n%s\n%s\n%s\n%s\n%s".formatted(
                                "[" + ni.getName() + "]",
                                "  status: " + (unavailableInterfaces.containsKey(ni.getName()) ? "🔴":"🟢"),
                                "  is_connected: "+ ni.isConnected(),
                                "  is_wireless: " + ni.isWireless(),
                                "  address: " + ni.getNetworkAddress(),
                                "  wireless_info: " + ni.getWirelessInfo()
                        )
                ).collect(Collectors.joining("\n"));
    }

    //주기적으로 네트워크 인터페이스 정보를 가져와서 캐시함
    //변경되면 이벤트를 발행
    @Scheduled(fixedRate = 3000)
    public void refresh() {
        Map<String, NetworkInterface> oldInterfaces = Map.copyOf(this.nowInterfaces);
        this.nowInterfaces = fetch();
        checkChanged(oldInterfaces);
    }

    // 이전과 새로 조회된 네트워크 인터페이스를 비교하여 변경점이 있으면 이벤트 발생
    private void checkChanged(Map<String, NetworkInterface> oldInterfaces) {
        // 이전/현재 모든 인터페이스들
        Set<String> oldSet = new HashSet<>(oldInterfaces.keySet());
        Set<String> nowSet = new HashSet<>(nowInterfaces.keySet());
        // 이전에 존재했던 managed들
        Set<String> oldManagedNIs = oldSet.stream()
                .filter(managedProfiles::contains)
                .collect(Collectors.toSet());
        // 현재 존재하는 managed들
        Set<String> nowManagedNIs = nowSet.stream()
                .filter(managedProfiles::contains)
                .collect(Collectors.toSet());
        // 이전에 존재했던 monitor
        Optional<String> oldMonitorNI = oldSet.stream().filter(monitorProfile::equals).findAny();
        // 현재 존재하는 monitor
        Optional<String> nowMonitorNI = nowSet.stream().filter(monitorProfile::equals).findAny();

        // 1. 제거된 모든 인터페이스들 처리 (managed, monitor 구별 없이 똑같이 처리)
        checkRemoved(oldSet, nowSet, oldInterfaces);
        // 2-1. 새로 추가된 managed 인터페이스들 처리 (현재에는 있으나 기존에는 없는 경우)
        checkAddedManagedNIs(oldManagedNIs, nowManagedNIs);
        // 2-2. 새로 추가된 monitor 인터페이스 처리
        nowMonitorNI.filter(ni -> oldMonitorNI.isEmpty()).ifPresent(this::checkAddedMonitorNI);
        // 3-1. 기존부터 존재하던 managed 인터페이스들 처리
        checkExistingManagedNIs(oldManagedNIs, nowManagedNIs, oldInterfaces);
        // 3-2. 기존부터 존재하던 monitor 인터페이스 처리
        nowMonitorNI.filter(ni -> oldMonitorNI.isPresent()).ifPresent(ni-> checkExistingMonitorNI(ni, oldInterfaces));
    }

    private void checkRemoved(
            Set<String> oldSet, Set<String> nowSet,
            Map<String, NetworkInterface> oldInterfaces){
        Set<String> removed = new HashSet<>(oldSet);
        removed.removeAll(nowSet);
        for (String niName : removed) {
            NetworkInterface oldInterface = oldInterfaces.get(niName);
            unavailableInterfaces.put(niName, REMOVED);
            log.warn("인터페이스 {}가 사라졌습니다.", niName);
            eventPublisher.publishEvent(
                    new NetworkInterfaceStatusEvent(niName, oldInterface, null, REMOVED)
            );
        }
    }

    private void checkAddedManagedNIs(Set<String> oldManagedNIs, Set<String> nowManagedNIs){
        Set<String> addedManagedNIs = new HashSet<>(nowManagedNIs);
        addedManagedNIs.removeAll(oldManagedNIs);
        for (String niName : addedManagedNIs) {
            NetworkInterface nowInterface = nowInterfaces.get(niName);
            if (nowInterface.isConnected()) {
                unavailableInterfaces.remove(niName);
                log.info("{}가 추가되고 연결되었습니다.", niName);
                eventPublisher.publishEvent(
                        new NetworkInterfaceStatusEvent(niName, null, nowInterface, ADDED_AND_RECONNECTED)
                );
            } else {
                unavailableInterfaces.put(niName, ADDED);
                log.info("{}가 새로 추가되었습니다.", niName);
                eventPublisher.publishEvent(
                        new NetworkInterfaceStatusEvent(niName, null, nowInterface, ADDED)
                );
            }
        }
    }

    private void checkAddedMonitorNI(String addedMonitorNI){
        NetworkInterface nowInterface = nowInterfaces.get(addedMonitorNI);
        unavailableInterfaces.remove(addedMonitorNI);
        log.info("{}가 새로 추가되었습니다.", addedMonitorNI);
        eventPublisher.publishEvent(
                new NetworkInterfaceStatusEvent(addedMonitorNI, null, nowInterface, ADDED)
        );
    }

    private void checkExistingManagedNIs(
            Set<String> oldManagedNIs, Set<String> nowManagedNIs,
            Map<String, NetworkInterface> oldInterfaces){
        Set<String> existingManagedNIs = new HashSet<>(nowManagedNIs);
        existingManagedNIs.retainAll(oldManagedNIs);
        for (String niName : existingManagedNIs) {
            NetworkInterface oldInterface = oldInterfaces.get(niName);
            NetworkInterface nowInterface = nowInterfaces.get(niName);

            // 연결 상태 변화
            if (oldInterface.isConnected() && !nowInterface.isConnected()) {
                unavailableInterfaces.put(niName, DISCONNECTED);
                log.warn("{}의 네트워크 연결이 끊겼습니다.", niName);
                eventPublisher.publishEvent(
                        new NetworkInterfaceStatusEvent(niName, oldInterface, nowInterface, DISCONNECTED)
                );
            } else if (!oldInterface.isConnected() && nowInterface.isConnected()) {
                unavailableInterfaces.remove(niName);
                log.info("{}가 다시 네트워크에 연결되었습니다.", niName);
                eventPublisher.publishEvent(
                        new NetworkInterfaceStatusEvent(niName, oldInterface, nowInterface, RECONNECTED)
                );
            }
        }
    }

    private void checkExistingMonitorNI(String niName, Map<String, NetworkInterface> oldInterfaces){
        // 무선 모드 변화 감지
        NetworkInterface oldInterface = oldInterfaces.get(niName);
        NetworkInterface nowInterface = nowInterfaces.get(niName);
        WirelessMode oldMode = oldInterface.getWirelessInfo().mode();
        WirelessMode nowMode = nowInterface.getWirelessInfo().mode();
        if (Objects.equals(oldMode, nowMode)) return;

        if (nowMode == MANAGED){
            // monitor여야 하는게 managed로 바뀐거니 에러로 판단
            unavailableInterfaces.put(niName, MODE_CHANGED);
            log.warn("{}의 무선 모드가 변경되었습니다. {} -> {}",
                    niName,
                    oldInterface.getWirelessInfo(),
                    nowInterface.getWirelessInfo());
        } else {
            // monitor여야 하는게 monitor로 바뀐거니 정상화된 것
            unavailableInterfaces.remove(niName);
            log.info("{}의 무선 모드가 변경되었습니다. {} -> {}",
                    niName,
                    oldInterface.getWirelessInfo(),
                    nowInterface.getWirelessInfo());
        }
        eventPublisher.publishEvent(
                new NetworkInterfaceStatusEvent(niName, oldInterface, nowInterface, MODE_CHANGED)
        );
    }

    // 최신 네트워크 인터페이스 정보를 반환
    private Map<String, NetworkInterface> fetch(){
        Map<String, NetworkAddress> connectionInfos = networkAddressResolver.resolve();
        Map<String, WirelessInfo> wirelessInfos = wirelessInfoResolver.resolve();

        //가져온 모든 인터페이스
        Set<String> fetchedInterfaces = new HashSet<>();
        fetchedInterfaces.addAll(connectionInfos.keySet());
        fetchedInterfaces.addAll(wirelessInfos.keySet());

        //모든 인터페이스 중 프로파일로 설정된 인터페이스만 걸러냄
        return allProfiles.stream()
                .filter(fetchedInterfaces::contains)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                interfaceName -> interfaceName,
                                interfaceName -> NetworkInterface.of(
                                        interfaceName,
                                        connectionInfos.get(interfaceName),
                                        wirelessInfos.get(interfaceName)
                                ),
                                (existing, replacement) -> existing  // 중복 시 기존 값 유지
                        ),
                        Collections::unmodifiableMap
                        )
                );
    }
}
