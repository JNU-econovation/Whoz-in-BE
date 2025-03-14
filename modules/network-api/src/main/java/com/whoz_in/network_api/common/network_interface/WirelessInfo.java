package com.whoz_in.network_api.common.network_interface;

import jakarta.annotation.Nullable;

public record WirelessInfo(
        String mode,
        @Nullable String ssid // mode가 "managed"일 때 사용
) {}
