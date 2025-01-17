package com.whoz_in.main_api.shared.caching.device;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public final class TempDeviceInfo {
    private final String room;
    private final String ssid;
    @EqualsAndHashCode.Exclude
    private final String mac;
}
