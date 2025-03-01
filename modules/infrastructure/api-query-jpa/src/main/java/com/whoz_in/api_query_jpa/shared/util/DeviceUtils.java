package com.whoz_in.api_query_jpa.shared.util;

import com.whoz_in.api_query_jpa.device.DeviceRepository;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeviceUtils {

    private final ActiveDeviceRepository activeDeviceRepository;
    private final DeviceRepository deviceRepository;
    private final MemberRepository memberRepository;

    public boolean isLastDevice(UUID deviceId) {
        // 마지막으로 접속 종료된 기기인지 검증

        Member owner = memberRepository.findByDeviceId(deviceId).orElse(null);

        if(owner==null) return false;

        List<ActiveDeviceEntity> myDevices = findMyActiveDevices(owner.getId());

        List<UUID> lastDevices = findLastDevice(myDevices);

        return lastDevices.contains(deviceId);

    }

    public List<ActiveDeviceEntity> findMyActiveDevices(UUID ownerId){
        return activeDeviceRepository.findByMemberId(ownerId);
    }

    public List<UUID> findLastDevice(List<ActiveDeviceEntity> activeDevices){
        List<UUID> lastDevices = new ArrayList<>();

        // disconnect 시간을 기준으로 가장 늦게 연결이 종료된 기기 리턴
        LocalDateTime lastDisconnectedAt = activeDevices.stream()
                .max(DeviceTimeComparators.disConnectedTimeComparator())
                .map(ActiveDeviceEntity::getDisConnectedAt)
                .orElse(null);

        if(activeDevices.isEmpty()) return lastDevices;

        lastDevices.addAll(
                activeDevices.stream()
                        .filter(activeDevice -> activeDevice.getDisConnectedAt().isEqual(lastDisconnectedAt))
                        .map(ActiveDeviceEntity::getDeviceId)
                        .toList()
        );

        return lastDevices;
    }

}
