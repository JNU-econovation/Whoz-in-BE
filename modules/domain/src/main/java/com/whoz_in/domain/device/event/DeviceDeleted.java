package com.whoz_in.domain.device.event;

import com.whoz_in.domain.shared.event.DomainEvent;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class DeviceDeleted extends DomainEvent {

    private final UUID deviceId;

}
