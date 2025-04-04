package com.whoz_in.shared.domain_event.device;

import com.whoz_in.shared.domain_event.DomainEvent;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class DeviceCreated extends DomainEvent {
    private final UUID deviceId;
    private final UUID memberId;
    private final String deviceName;
    private final List<DeviceInfoPayload> deviceInfos;
}
