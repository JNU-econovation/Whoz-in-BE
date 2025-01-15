package com.whoz_in.domain.device;

import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import java.util.Optional;

public interface DeviceRepository {
    void save(Device device);

    //해당 mac을 포함하는 device를 찾는 메서드
    Optional<Device> findByMac(String mac);
    Optional<Device> findByDeviceId(DeviceId deviceId);
    default Device getByDeviceId(DeviceId deviceId){
        return findByDeviceId(deviceId).orElseThrow(()->new IllegalArgumentException("device가 없음"));
    }
}
