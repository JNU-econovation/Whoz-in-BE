package com.whoz_in.shared.domain_event.device;

import com.whoz_in.shared.domain_event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class DeviceInfoUpdated extends DomainEvent {
    private final String room;
    private final String ssid;
    private final String mac;
}
