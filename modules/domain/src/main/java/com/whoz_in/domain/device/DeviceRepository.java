package com.whoz_in.domain.device;

import com.whoz_in.domain.device.model.Device;
import java.util.List;

public interface DeviceRepository {
    void save(Device device);
    List<Device> findAll();
}
