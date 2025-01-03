package com.whoz_in.domain.network_log;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public final class ManagedLog {
    private final String mac;
    private final String ip;
    private final String deviceName;
    private final String ssid;
    private final String room;
    private final LocalDateTime createdAt;
}
