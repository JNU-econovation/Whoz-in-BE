package com.whoz_in.main_api.query.device.application.active.event;

import com.whoz_in.domain.device.model.Device;
import com.whoz_in.main_api.shared.event.Event;
import java.util.List;
import lombok.Getter;

@Getter
public class InActiveDeviceFinded implements Event {

    private final List<Device> devices;

    public InActiveDeviceFinded(List<Device> devices) {
        this.devices = devices;
    }

}
