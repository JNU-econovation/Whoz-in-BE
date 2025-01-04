package com.whoz_in.network_api.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public final class NetworkInterface{
    @EqualsAndHashCode.Include
    private final String name;
    @EqualsAndHashCode.Include
    private final String essid;
    @EqualsAndHashCode.Include
    private final String mode;

    @Override
    public String toString() {
        return "name: " + name +", ssid: "+ essid + ", mode: "+ mode;
    }
}