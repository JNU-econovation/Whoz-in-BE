package com.whoz_in.network_api.common.network_interface.nux;

import com.whoz_in.network_api.common.network_interface.NetworkAddress;
import com.whoz_in.network_api.common.network_interface.NetworkAddressResolver;
import com.whoz_in.network_api.common.process.TransientProcess;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Profile("prod")
@Component
public final class NmcliNetworkAddressResolver implements NetworkAddressResolver {
    private static final Pattern IP_PATTERN = Pattern.compile(
            "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$" // IPv4 주소 형식
    );

    @Override
    public Map<String, NetworkAddress> resolve() {
        return parse(TransientProcess.create("nmcli device show").result());
    }

    private static Map<String, NetworkAddress> parse(String result) {
        Map<String, NetworkAddress> connectionInfoMap = new HashMap<>();

        String[] lines = result.split("\n");
        String currentDevice = null;
        String ipAddress = null;
        String gateway = null;

        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("GENERAL.DEVICE:")) {
                // 이전 네트워크 인터페이스가 유효하면 저장
                if (currentDevice != null && isValidIp(ipAddress) && isValidIp(gateway)) {
                    connectionInfoMap.put(currentDevice, new NetworkAddress(ipAddress, gateway));
                }

                // 새 네트워크 인터페이스 초기화
                currentDevice = line.split(":")[1].trim();
                ipAddress = null;
                gateway = null;
            } else if (line.startsWith("IP4.ADDRESS[") && ipAddress == null) {
                String[] parts = line.split(":");
                if (parts.length > 1) {
                    ipAddress = parts[1].trim().split("/")[0]; // 서브넷 마스크 제거
                }
            } else if (line.startsWith("IP4.GATEWAY:") && gateway == null) {
                String[] parts = line.split(":");
                if (parts.length > 1) {
                    gateway = parts[1].trim();
                }
            }
        }

        // 마지막 네트워크 장치 추가 (반복문이 끝난 후 처리)
        if (currentDevice != null && isValidIp(ipAddress) && isValidIp(gateway)) {
            connectionInfoMap.put(currentDevice, new NetworkAddress(ipAddress, gateway));
        }

        return connectionInfoMap;
    }

    private static boolean isValidIp(String ip) {
        return ip != null && IP_PATTERN.matcher(ip).matches();
    }
}
