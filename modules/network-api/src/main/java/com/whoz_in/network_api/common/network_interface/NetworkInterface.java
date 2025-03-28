package com.whoz_in.network_api.common.network_interface;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


// 네트워크 인터페이스를 network-api에서 필요한대로 가공
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class NetworkInterface{
    @EqualsAndHashCode.Include
    private final String name;
    @Nullable private NetworkAddress networkAddress; // null일 경우 네트워크에 연결되지 않음
    @Nullable private WirelessInfo wirelessInfo; // null일 경우 유선

    public boolean isConnected(){
        return networkAddress != null;
    }
    public boolean isWireless(){
        return wirelessInfo != null;
    }

    public static NetworkInterface of(String interfaceName, @Nullable NetworkAddress networkAddress, @Nullable WirelessInfo wirelessInfo){
        return new NetworkInterface(interfaceName, networkAddress, wirelessInfo);
    }
}
