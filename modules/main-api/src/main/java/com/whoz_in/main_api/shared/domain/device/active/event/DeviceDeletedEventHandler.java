package com.whoz_in.main_api.shared.domain.device.active.event;

import com.whoz_in.domain.device.event.DeviceDeleted;
import com.whoz_in.main_api.shared.event.Events;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DeviceDeletedEventHandler {

    @TransactionalEventListener(DeviceDeleted.class)
    public void processDeviceDeletedEvent(DeviceDeleted event) {
        UUID deviceId = event.getDeviceId();

        Events.raise(new DeviceDeletedEvent(deviceId));
    }
}
