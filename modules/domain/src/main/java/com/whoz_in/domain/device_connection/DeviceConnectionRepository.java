package com.whoz_in.domain.device_connection;

import com.whoz_in.domain.device.model.DeviceId;
import java.util.List;
import java.util.Optional;

public interface DeviceConnectionRepository {
    void save(DeviceConnection connection);
    Optional<DeviceConnection> findLatestByDeviceId(DeviceId id);
    Optional<DeviceConnection> findConnectedByDeviceId(DeviceId id);
    List<DeviceConnection> findAllConnected(); // 하루 내에서만 찾으면 됨
}
