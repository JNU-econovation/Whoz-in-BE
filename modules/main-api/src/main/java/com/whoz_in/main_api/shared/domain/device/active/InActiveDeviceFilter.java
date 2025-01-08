package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.query.device.application.active.ActiveDeviceViewer;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class InActiveDeviceFilter extends DeviceFilter{

    public InActiveDeviceFilter(
            DeviceRepository deviceRepository,
            MonitorLogRepository monitorLogRepository,
            ActiveDeviceViewer activeDeviceViewer) {
        super(deviceRepository, monitorLogRepository, activeDeviceViewer);
    }

    @Override
    protected List<Device> find() {
        return List.of();
    }

    @Override
    protected boolean judge(Device device) {
        return false;
    }

    @Override
    protected void raiseEvent(List<Device> devices) {

    }
}
