package com.whoz_in.api_query_jpa.device.active.event;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.main_api.shared.domain.device.active.event.DeviceDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("ActiveDeviceDeletedEventHandler")
@RequiredArgsConstructor
public class ActiveDeviceDeletedEventHandler {

    private final ActiveDeviceRepository activeDeviceRepository;

    @Transactional
    @EventListener(DeviceDeletedEvent.class)
    public void handle(DeviceDeletedEvent event) {
        activeDeviceRepository.deleteById(event.deviceId());
    }

}
