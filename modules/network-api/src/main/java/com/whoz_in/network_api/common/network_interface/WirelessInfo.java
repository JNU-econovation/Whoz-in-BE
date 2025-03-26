package com.whoz_in.network_api.common.network_interface;

import jakarta.annotation.Nullable;

public record WirelessInfo(
        WirelessMode mode,
        @Nullable String ssid // mode가 MANAGED 일 때 사용
) {}
