package com.whoz_in.network_api.common.network_interface;

import java.util.regex.Pattern;

public record NetworkAddress(String ip, String gateway) {
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$"
    );

    public NetworkAddress {
        if (!isValid(ip) || !isValid(gateway)) {
            throw new IllegalArgumentException("잘못된 IPv4 주소 형식: " + ip + ", " + gateway);
        }
    }

    private static boolean isValid(String address) {
        return address != null && IPV4_PATTERN.matcher(address).matches();
    }
}
