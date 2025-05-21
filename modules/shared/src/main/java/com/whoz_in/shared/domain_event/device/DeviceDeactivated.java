package com.whoz_in.shared.domain_event.device;

import com.whoz_in.shared.domain_event.DomainEvent;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class DeviceDeactivated extends DomainEvent {

    private final UUID deviceId;
    private final UUID memberId;
}
