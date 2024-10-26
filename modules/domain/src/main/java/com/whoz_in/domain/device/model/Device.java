package com.whoz_in.domain.device.model;

import com.whoz_in.domain.device.event.DeviceCreated;
import com.whoz_in.domain.shared.AggregateRoot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Device extends AggregateRoot {
    private Long deviceId;
    private Long memberId;
    private MacAddress mac;
    private IpAddress ip;

    public static Device create(Long memberId, MacAddress mac, IpAddress ip){
        Device device = Device.builder()
                .memberId(memberId)
                .mac(mac)
                .ip(ip)
                .build();
        device.register(new DeviceCreated());
        return device;
    }

    public static Device load(Long deviceId, Long memberId, String macAddress, String ipAddress){
        return Device.builder()
                .deviceId(deviceId)
                .memberId(memberId)
                .mac(MacAddress.load(macAddress))
                .ip(IpAddress.load(ipAddress))
                .build();
    }
}
