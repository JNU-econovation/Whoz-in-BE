package com.whoz_in.api_query_jpa.shared.util;

import com.whoz_in.api_query_jpa.device.DeviceRepository;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public boolean isLastDisConnectedDevice(UUID deviceId) {
        // 마지막으로 접속 종료된 기기인지 검증
        Member owner = memberRepository.getByDeviceId(deviceId);

        List<ActiveDeviceEntity> myDevices = activeDeviceRepository.findByMemberId(owner.getId());

        List<UUID> lastDevices = findLastDisConnectedDevice(myDevices);

        return lastDevices.contains(deviceId);

    }

    public List<UUID> findLastDisConnectedDevice(List<ActiveDeviceEntity> activeDevices){
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

    public boolean isLongest(UUID deviceId) {
        // 가장 오랫동안 있었는지 검증
        Member owner = memberRepository.getByDeviceId(deviceId);

        List<ActiveDeviceEntity> activeDevices = activeDeviceRepository.findByMemberId(owner.getId());

        List<UUID> longestDeviceIds = findLongestDevice(activeDevices);

        return longestDeviceIds.contains(deviceId);
    }

    public List<UUID> findLongestDevice(List<ActiveDeviceEntity> activeDevices){
        List<UUID> longestDevices = new ArrayList<>();
        // 접속 시간을 기준으로 가장 오래된 기기를 찾는다.
         Duration maxContinuousTime = activeDevices.stream()
                .max(DeviceTimeComparators.continuousTimeComparator())
                .map(ActiveDeviceEntity::getContinuousTime)
                .orElse(null);

        if(activeDevices.isEmpty()) return longestDevices;

        longestDevices.addAll(
                activeDevices.stream()
                        .filter(activeDevice -> activeDevice.getContinuousTime().equals(maxContinuousTime))
                        .map(ActiveDeviceEntity::getDeviceId)
                        .toList()
        );

        return longestDevices;
    }

}
