package com.whoz_in.network_api.private_ip;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.Collections;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

//내부 아이피를 알아내는 클래스
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SystemPrivateIpResolver {
    public static Optional<String> getIPv4(String interfaceName) {
        try {
            return Collections.list(java.net.NetworkInterface.getNetworkInterfaces()).stream()
                    .filter(networkInterface -> networkInterface.getName().equals(interfaceName))
                    .flatMap(networkInterface -> Collections.list(networkInterface.getInetAddresses()).stream())
                    .filter(address -> !address.isLoopbackAddress() && address instanceof java.net.Inet4Address)
                    .map(InetAddress::getHostAddress)
                    .findAny();
        } catch (SocketException e) {
            throw new IllegalStateException("시스템에 네트워크 인터페이스가 아예 존재하지 않음");
        }
    }
}
