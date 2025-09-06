package com.whoz_in.shared.domain_event.device;

import com.whoz_in.shared.domain_event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class DeviceDeactivated extends DomainEvent {

    private final String deviceId;
    private final String memberId;
}
