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
    private Long id;
    private Long memberId;
    private MacAddress mac;
    private IpAddress ip;
    private String name;

    public static Device create(Long memberId, MacAddress mac, IpAddress ip, String name){
        Device device = Device.builder()
                .memberId(memberId)
                .mac(mac)
                .ip(ip)
                .name(name)
                .build();
        device.register(new DeviceCreated());
        return device;
    }

    public static Device load(Long id, Long memberId, String mac, String ip, String name){
        return Device.builder()
                .id(id)
                .memberId(memberId)
                .mac(MacAddress.load(mac))
                .ip(IpAddress.load(ip))
                .name(name)
                .build();
    }
}
