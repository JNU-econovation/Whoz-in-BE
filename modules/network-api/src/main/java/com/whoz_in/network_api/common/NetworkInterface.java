package com.whoz_in.network_api.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
public final class NetworkInterface{
    //
    @EqualsAndHashCode.Exclude
    private final String altSsid; //network log의 ssid는 이걸로 저장된다. (ex: econo)
    private final String interfaceName;
    private final String realSsid; //실제 연결될 와이파이 이름 (ex: ECONO_5G)
    private final String mode;

    public NetworkInterface(String altSsid, String interfaceName, String realSsid, String mode) {
        this.altSsid = altSsid;
        this.interfaceName = interfaceName;
        this.realSsid = realSsid;
        this.mode = mode;
    }

    public NetworkInterface(String interfaceName, String realSsid, String mode) {
        this(realSsid, interfaceName, realSsid, mode);
    }
}