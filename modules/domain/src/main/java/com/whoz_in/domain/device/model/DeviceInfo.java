package com.whoz_in.domain.device.model;


import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DeviceInfo {
    private final String room;
    private final String ssid;
    @EqualsAndHashCode.Exclude
    private final MacAddress mac;

    public static DeviceInfo create(String room, String ssid, MacAddress mac){
        return new DeviceInfo(room, ssid, mac);
    }
    public static DeviceInfo load(String room, String ssid, String mac){
        return new DeviceInfo(room, ssid, MacAddress.load(mac));
    }
}
