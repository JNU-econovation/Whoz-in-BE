package com.whoz_in.network_api.common.network_interface;

import jakarta.annotation.Nullable;
import java.net.Inet4Address;
import java.util.Optional;
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

    @Nullable private ConnectionInfo connectionInfo; // null일 경우 연결되지 않음 / 변경 가능
    @Nullable private final WirelessInfo wirelessInfo; // null일 경우 유선

    public boolean isConnected(){
        return connectionInfo != null;
    }
    public boolean isWireless(){
        return wirelessInfo != null;
    }

    public static NetworkInterface wired(String interfaceName, @Nullable ConnectionInfo connectionInfo){
        return new NetworkInterface(interfaceName, connectionInfo, null);
    }

    public static NetworkInterface wireless(String interfaceName, @Nullable ConnectionInfo connectionInfo, WirelessInfo wirelessInfo){
        return new NetworkInterface(interfaceName, connectionInfo, wirelessInfo);
    }

    public static NetworkInterface of(String interfaceName, @Nullable ConnectionInfo connectionInfo, @Nullable WirelessInfo wirelessInfo){
        return new NetworkInterface(interfaceName, connectionInfo, wirelessInfo);
    }
}