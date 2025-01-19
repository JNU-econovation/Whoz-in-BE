package com.whoz_in.domain.device.event;

import com.whoz_in.domain.shared.event.DomainEvent;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class DeviceCreated extends DomainEvent {
    private final UUID deviceId;
}
