package com.whoz_in.domain.device.model;

import com.whoz_in.domain.device.event.DeviceCreated;
import com.whoz_in.domain.device.event.DeviceInfoRegistered;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.shared.AggregateRoot;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Device extends AggregateRoot {
    private final DeviceId id;
    private final MemberId memberId; //양도 기능 생기면 final 제거
    private String deviceName;
    private final Set<DeviceInfo> deviceInfos;

    public boolean isOwnedBy(MemberId memberId){
        return this.getMemberId().equals(memberId);
    }

    public void registerDeviceInfo(DeviceInfo deviceInfo){
        this.deviceInfos.add(deviceInfo);
        this.register(new DeviceInfoRegistered());
    }

    public void registerDeviceInfo(Set<DeviceInfo> deviceInfos){
        deviceInfos.forEach(this::registerDeviceInfo);
    }

    public static Device create(MemberId memberId, Set<DeviceInfo> deviceInfos, String deviceName){
        Device device = Device.builder()
                .id(new DeviceId(UUID.randomUUID()))
                .memberId(memberId)
                .deviceInfos(deviceInfos)
                .deviceName(deviceName)
                .build();
        device.register(new DeviceCreated());
        return device;
    }

    public static Device load(DeviceId id, MemberId memberId, Set<DeviceInfo> deviceInfos, String deviceName){
        return Device.builder()
                .id(id)
                .memberId(memberId)
                .deviceInfos(new HashSet<>(deviceInfos))
                .deviceName(deviceName)
                .build();
    }
}
