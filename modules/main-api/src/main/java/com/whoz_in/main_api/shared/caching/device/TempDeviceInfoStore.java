package com.whoz_in.main_api.shared.caching.device;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.whoz_in.main_api.config.RoomSsidConfig;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 사전 지식: 사용자는 기기 등록을 위해선 해당 방에 존재하는 와이파이에 대한 맥을(DeviceInfo) 가지고 있어야 하며, 모두 가지면 기기 등록을 시작할 수 있다.
 * 책임: 따라서 이 클래스는 기기 등록 전까지 맥을 임시 저장 및 관리하는 책임을 가진다.
 * 주의: command, query side에서 공용으로 사용하는 것이므로 어느 한 쪽에 의존하는 클래스를 사용하지 않도록 한다.
 * 참고: 연결 시마다 맥이 바뀌도록 설정한 기기도 있기 때문에 시간 제한을 뒀다.
 */
@Component
@RequiredArgsConstructor
public final class TempDeviceInfoStore {
    private static final Cache<UUID, CopyOnWriteArrayList<TempDeviceInfo>> store = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES) // 5분 동안 접근이 없으면 만료
            .build();

    private final RoomSsidConfig ssidConfig;


    //이전에 등록되지 않은 TempDeviceInfo인지 검증
    public void mustNotExist(UUID ownerId, TempDeviceInfo deviceInfo){
        List<TempDeviceInfo> deviceInfos = store.getIfPresent(ownerId);
        if (deviceInfos != null && deviceInfos.stream().anyMatch(di->di.equals(deviceInfo)))
            throw new IllegalArgumentException("이미 등록됨");
    }

    //TODO: deviceInfo들에 room이 있는데 room을 굳이 받아야 하나
    //room의 모든 와이파이에 대해 CachedDevice가 추가되었는지 검증
    public void verifyAllAdded(UUID ownerId, String room){
        List<TempDeviceInfo> deviceInfos = get(ownerId);
        List<String> unregisteredSsids = ssidConfig.getSsids(room).stream()
                .filter(ssid -> deviceInfos.stream().noneMatch(di -> di.getSsid().equals(ssid)))
                .toList();
        if (!unregisteredSsids.isEmpty()) {
            throw new IllegalArgumentException("다음 wifi에 대해 맥을 등록하지 않았습니다. " + String.join(", ", unregisteredSsids));
        }
    }

    //DeviceInfo 추가
    public void add(UUID ownerId, TempDeviceInfo newDeviceInfo) {
        try {
            List<TempDeviceInfo> deviceInfos = store.get(ownerId, CopyOnWriteArrayList::new);
            deviceInfos.removeIf(di-> di.equals(newDeviceInfo));
            deviceInfos.add(newDeviceInfo);
        } catch (ExecutionException e) {
            throw new IllegalStateException("value 초기화 실패");
        }
    }

    //불변 반환
    public List<TempDeviceInfo> get(UUID ownerId){
        List<TempDeviceInfo> deviceInfos = store.getIfPresent(ownerId);
        return deviceInfos != null ? List.copyOf(deviceInfos) : List.of();
    }

    public void remove(UUID ownerId){
        store.invalidate(ownerId);
    }

    //반환 전 제거
    public List<TempDeviceInfo> takeout(UUID ownerId){
        List<TempDeviceInfo> deviceInfos = get(ownerId);
        remove(ownerId);
        return deviceInfos;
    }
}
