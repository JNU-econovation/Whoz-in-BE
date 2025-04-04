package com.whoz_in.main_api.shared.domain.device.active.event;

import com.whoz_in.main_api.shared.event.Events;
import com.whoz_in.shared.domain_event.device.DeviceCreated;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DeviceCreatedEventHandler {

    // TODO: 로직 구체화
    @TransactionalEventListener(DeviceCreated.class)
    public void processDeviceCreatedEvent(DeviceCreated event) {
        UUID deviceId = event.getDeviceId();

        Events.raise(new ActiveDeviceFound(List.of(deviceId)));
    }

}
