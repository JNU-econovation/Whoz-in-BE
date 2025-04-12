package com.whoz_in.domain.device;

import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.member.model.MemberId;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository {
    void save(Device device);
    //해당 mac을 포함하는 device를 찾는 메서드
    Optional<Device> findByMac(String mac);
    Optional<Device> findByDeviceId(DeviceId deviceId);
    List<Device> findByMacs(List<String> macs);
    List<Device> findByDeviceIds(Collection<DeviceId> deviceIds);
    List<Device> findByOwnerId(MemberId ownerId);
}
