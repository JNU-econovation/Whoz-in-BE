package com.whoz_in.main_api.shared.domain.device.active.event;

import com.whoz_in.domain.device.active.ActiveDeviceRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ActiveDeviceEventHandler {

    private final ActiveDeviceRepository repository;

    public ActiveDeviceEventHandler(ActiveDeviceRepository repository){
        this.repository = repository;
    }

    @Async
    @EventListener(ActiveDeviceFinded.class)
    public void onActiveDeviceFinded(ActiveDeviceFinded event) {
        repository.saveAll(event.getDevices());
    }

}
