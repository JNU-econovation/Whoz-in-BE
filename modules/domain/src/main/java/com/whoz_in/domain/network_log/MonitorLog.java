package com.whoz_in.domain.network_log;


import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class MonitorLog {
    private final String mac;
    private final LocalDateTime updatedAt;
}
