package com.whoz_in.main_api.shared.domain.device.active.event;

import com.whoz_in.main_api.shared.event.ApplicationEvent;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class InActiveDeviceFinded implements ApplicationEvent {

    private final List<UUID> devices;

    public InActiveDeviceFinded(List<UUID> devices) {
        this.devices = devices;
    }

}
