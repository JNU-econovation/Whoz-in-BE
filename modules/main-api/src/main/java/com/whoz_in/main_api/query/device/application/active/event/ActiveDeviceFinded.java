package com.whoz_in.main_api.query.device.application.active.event;

import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.main_api.query.device.application.active.ActiveDevice;
import com.whoz_in.main_api.shared.event.Event;
import java.util.List;
import lombok.Getter;

@Getter
public class ActiveDeviceFinded implements Event {

    private final List<Device> devices;
    private final List<MonitorLog> logs;

    public ActiveDeviceFinded(List<Device> devices, List<MonitorLog> logs) {
        this.devices = devices;
        this.logs = logs;
    }

}
