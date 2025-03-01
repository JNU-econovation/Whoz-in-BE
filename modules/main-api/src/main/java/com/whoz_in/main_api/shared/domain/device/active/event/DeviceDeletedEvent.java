package com.whoz_in.main_api.shared.domain.device.active.event;

import com.whoz_in.main_api.shared.event.ApplicationEvent;
import java.util.UUID;

public record DeviceDeletedEvent(
    UUID deviceId
)
        implements ApplicationEvent {
}
