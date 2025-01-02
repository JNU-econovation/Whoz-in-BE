package com.whoz_in.domain.device.model;

import com.whoz_in.domain.device.exception.InvalidIPv4AddressException;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class IpAddress {
    //IPv4 형식에 맞는지 검증하는 패턴
    private static final Pattern pattern = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
    );

    private final String address;

    public static IpAddress create(String address){
        if (!isValid(address)) {
            throw new InvalidIPv4AddressException();
        }
        return new IpAddress(address);
    }

    static IpAddress load(String address){
        return new IpAddress(address);
    }

    private static boolean isValid(String address) {
        return pattern.matcher(address).matches();
    }

    @Override
    public String toString() {
        return this.address;
    }
}

