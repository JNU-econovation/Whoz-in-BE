package com.whoz_in.main_api.shared.domain.device.active.event;

import com.whoz_in.domain.device.event.DeviceCreated;
import com.whoz_in.main_api.shared.event.Events;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DeviceCreatedEventHandler {

    // TODO: 로직 구체화
    @TransactionalEventListener(DeviceCreated.class)
    public void processDeviceCreatedEvent(DeviceCreated event) {
        UUID deviceId = event.getDeviceId();

        Events.raise(new DeviceCreatedEvent(deviceId));
    }

}
