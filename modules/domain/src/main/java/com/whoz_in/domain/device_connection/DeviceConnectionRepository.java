package com.whoz_in.domain.device_connection;

import com.whoz_in.domain.device.model.DeviceId;
import java.util.List;
import java.util.Optional;

public interface DeviceConnectionRepository {
    void save(DeviceConnection connection);
    Optional<DeviceConnection> findConnectedByDeviceId(DeviceId id);
    List<DeviceConnection> findAllConnected();
}
