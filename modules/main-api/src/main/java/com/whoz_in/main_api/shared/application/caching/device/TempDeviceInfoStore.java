package com.whoz_in.main_api.shared.application.caching.device;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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


    // 이전에 등록된 ssid인지 확인
    public boolean existsBySsid(UUID ownerId, String ssid){
        List<TempDeviceInfo> deviceInfos = store.getIfPresent(ownerId);
        return (deviceInfos != null) && deviceInfos.stream().anyMatch(di->di.getSsid().equals(ssid));
    }
    public boolean existsByMac(UUID ownerId, String mac){
        List<TempDeviceInfo> deviceInfos = store.getIfPresent(ownerId);
        return (deviceInfos != null) && deviceInfos.stream().anyMatch(di->di.getMac().equals(mac));
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

    //반환 전 제거
    //값이 없을경우 예외 발생 (동시 접근 시 뜰거임)
    public List<TempDeviceInfo> takeout(UUID ownerId){
        List<TempDeviceInfo> deviceInfos = store.asMap().remove(ownerId);
        if (deviceInfos == null) throw new IllegalStateException("저장된 device info가 없음");
        return deviceInfos;
    }

    public void remove(UUID ownerId){
        store.asMap().remove(ownerId);
    }
}
