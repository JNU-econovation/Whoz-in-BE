package com.whoz_in.shared.domain_event.device_connection;

import com.whoz_in.shared.domain_event.DomainEvent;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class DeviceDisconnected extends DomainEvent {
    private final UUID deviceConnectionId;
    private final UUID deviceId;
    private final String connectedRoom;
    private final LocalDateTime connectedAt;
    private final LocalDateTime disconnectedAt;
}
