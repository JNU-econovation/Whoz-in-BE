package com.whoz_in.domain.device.model;

import com.whoz_in.domain.device.event.DeviceCreated;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.shared.AggregateRoot;
import java.util.List;
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
    private String name;
    private final List<DeviceInfo> deviceInfos;

    public static Device create(MemberId memberId, List<DeviceInfo> deviceInfos, String name){
        Device device = Device.builder()
                .memberId(memberId)
                .deviceInfos(deviceInfos)
                .name(name)
                .build();
        device.register(new DeviceCreated());
        return device;
    }

    public static Device load(DeviceId id, MemberId memberId, List<DeviceInfo> deviceInfos, String name){
        return Device.builder()
                .id(id)
                .memberId(memberId)
                .deviceInfos(deviceInfos)
                .name(name)
                .build();
    }
}
