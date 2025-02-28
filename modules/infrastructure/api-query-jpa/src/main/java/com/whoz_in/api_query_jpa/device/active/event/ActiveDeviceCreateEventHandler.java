package com.whoz_in.api_query_jpa.device.active.event;


import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.main_api.shared.domain.device.active.event.DeviceCreatedEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ActiveDeviceCreateEventHandler {

    private final ActiveDeviceRepository activeDeviceRepository;

    @EventListener(DeviceCreatedEvent.class)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void handle(DeviceCreatedEvent event) {
        UUID deviceId = event.deviceId();

        ActiveDeviceEntity activeDevice = ActiveDeviceEntity.create(deviceId);

        activeDeviceRepository.save(activeDevice);
    }

}
