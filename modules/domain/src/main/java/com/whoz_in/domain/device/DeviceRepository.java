package com.whoz_in.domain.device;

import com.whoz_in.domain.device.model.Device;
import java.util.List;
import com.whoz_in.domain.device.model.DeviceId;
import java.util.Optional;

public interface DeviceRepository {
    void save(Device device);
    void delete(DeviceId deviceId);
    List<Device> findAll(); //TODO: 모든 Device를 찾아야 하는 이유
    //해당 mac을 포함하는 device를 찾는 메서드
    Optional<Device> findByMac(String mac);
    Optional<Device> findByDeviceId(DeviceId deviceId);
}
