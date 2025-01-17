package com.whoz_in.domain.device.model;

import com.whoz_in.domain.device.exception.InvalidMacAddressException;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class MacAddress {
    //맥 주소 형식에 맞는지 검증하기 위한 패턴
    private static final Pattern pattern = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");

    private final String address;

    public static MacAddress create(String address){
        if (!isValid(address)) {
            throw InvalidMacAddressException.EXCEPTION;
        }
        return new MacAddress(normalize(address));
    }

    static MacAddress load(String address){
        return new MacAddress(address);
    }

    private static boolean isValid(String address) {
        return pattern.matcher(address).matches();
    }

    private static String normalize(String address){
        return address.toUpperCase().replace("-", ":");
    }

    @Override
    public String toString() {
        return this.address;
    }
}
