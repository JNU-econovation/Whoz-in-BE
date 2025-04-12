package com.whoz_in.domain.device.model;

import com.whoz_in.domain.device.exception.DeviceInfoAlreadyRegisteredException;
import com.whoz_in.domain.device.exception.NoDeviceInfoException;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.shared.AggregateRoot;
import com.whoz_in.shared.Nullable;
import com.whoz_in.shared.domain_event.device.DeviceCreated;
import com.whoz_in.shared.domain_event.device.DeviceDeactivated;
import com.whoz_in.shared.domain_event.device.DeviceInfoPayload;
import com.whoz_in.shared.domain_event.device.DeviceInfoRegistered;
import com.whoz_in.shared.domain_event.device.DeviceInfoUpdated;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Device extends AggregateRoot {
    @Getter private final DeviceId id;
    @Getter private final MemberId memberId; //양도 기능 생기면 final 제거
    @Getter private String deviceName;
    private final Set<DeviceInfo> deviceInfos;
    @Getter @Nullable private LocalDateTime deactivatedAt;

    public boolean isDeactivated() {
        return deactivatedAt != null;
    }

    public void deactivate(){
        if (isDeactivated()) return;
        this.deactivatedAt = LocalDateTime.now();
        this.register(new DeviceDeactivated(id.id(), memberId.id()));
    }

    public Set<DeviceInfo> getDeviceInfos(){
        return Set.copyOf(deviceInfos);
    }

    public boolean isOwnedBy(MemberId memberId){
        return this.memberId.equals(memberId);
    }

    public void registerDeviceInfo(DeviceInfo deviceInfo){
        if (this.deviceInfos.contains(deviceInfo)) //이미 존재하면 예외
            throw DeviceInfoAlreadyRegisteredException.of(deviceInfo.getSsid());
        this.deviceInfos.add(deviceInfo);
        this.register(new DeviceInfoRegistered(
                deviceInfo.getSsid(),
                deviceInfo.getRoom(),
                deviceInfo.getMac().toString()
        ));
    }

    public void updateDeviceInfo(DeviceInfo deviceInfo){
        if (!deviceInfos.remove(deviceInfo)) { // 제거를 시도함. 존재하지 않으면 예외
            throw NoDeviceInfoException.of(deviceInfo.getSsid());
        }
        this.deviceInfos.add(deviceInfo);
        this.register(new DeviceInfoUpdated(
                deviceInfo.getSsid(),
                deviceInfo.getRoom(),
                deviceInfo.getMac().toString()
        ));
    }

    public static Device create(MemberId memberId, Set<DeviceInfo> deviceInfos, String deviceName){
        Device device = Device.builder()
                .id(new DeviceId(UUID.randomUUID()))
                .memberId(memberId)
                .deviceInfos(deviceInfos)
                .deviceName(deviceName)
                .deactivatedAt(null)
                .build();
        device.register(new DeviceCreated(
                device.getId().id(),
                device.getMemberId().id(),
                device.getDeviceName(),
                device.getDeviceInfos().stream()
                        .map(info -> new DeviceInfoPayload(
                                info.getSsid(),
                                info.getRoom(),
                                info.getMac().toString()
                        ))
                        .toList()
        ));
        return device;
    }

    public static Device load(DeviceId id, MemberId memberId, Set<DeviceInfo> deviceInfos,
            String deviceName, @Nullable LocalDateTime deactivatedAt){
        return Device.builder()
                .id(id)
                .memberId(memberId)
                .deviceInfos(new HashSet<>(deviceInfos))
                .deviceName(deviceName)
                .deactivatedAt(deactivatedAt)
                .build();
    }
}
