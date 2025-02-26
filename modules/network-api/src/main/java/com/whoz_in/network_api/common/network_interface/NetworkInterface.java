package com.whoz_in.network_api.common.network_interface;

import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class NetworkInterface{
    private final String interfaceName;
    private final String realSsid; //실제 연결될 와이파이 이름 (ex: ECONO_5G)
    private final String mode;

    @EqualsAndHashCode.Exclude
    @Nullable private final String altSsid; //network log의 ssid는 이걸로 저장된다. (ex: econo)
    @EqualsAndHashCode.Exclude
    @Nullable private final String command; //실행할 명령어

    public NetworkInterface(String interfaceName, String realSsid, String mode, String altSsid, String command) {
        this.interfaceName = interfaceName;
        this.realSsid = realSsid;
        this.mode = mode;
        this.altSsid = altSsid;
        this.command = command;
    }

    public NetworkInterface(String interfaceName, String realSsid, String mode) {
        this(interfaceName, realSsid, mode, realSsid, null);
    }
}