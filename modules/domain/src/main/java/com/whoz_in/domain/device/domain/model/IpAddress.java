package com.whoz_in.domain.device.domain.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class IpAddress {
    private final String address;

    public static IpAddress create(String address){
        //TODO: 검증 로직
        return new IpAddress(address);
    }

    static IpAddress load(String address){
        return new IpAddress(address);
    }
}
