package com.whoz_in.main_api.query.device.application.active.event;

import com.whoz_in.domain.device.model.Device;
import com.whoz_in.main_api.shared.event.Event;
import java.util.List;
import lombok.Getter;

@Getter
public class ActiveDeviceFinded implements Event {

    private final List<Device> devices;

    public ActiveDeviceFinded(List<Device> devices) {
        this.devices = devices;
    }

    public static ActiveDeviceFinded of(List<Device> devices) {
        return new ActiveDeviceFinded(devices);
    }


}
