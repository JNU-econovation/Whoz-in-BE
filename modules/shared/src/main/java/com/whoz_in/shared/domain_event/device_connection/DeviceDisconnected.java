package com.whoz_in.shared.domain_event.device_connection;

import com.whoz_in.shared.domain_event.DomainEvent;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class DeviceDisconnected extends DomainEvent {
    private final String deviceConnectionId;
    private final String deviceId;
    private final String connectedRoom;
    private final LocalDateTime connectedAt;
    private final LocalDateTime disconnectedAt;
}
