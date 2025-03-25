package com.whoz_in.domain.device;

import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.member.model.MemberId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DeviceRepository {
    void save(Device device);
    boolean delete(DeviceId deviceId);
    //해당 mac을 포함하는 device를 찾는 메서드
    Optional<Device> findByMac(String mac);
    Optional<Device> findByDeviceId(DeviceId deviceId);
    List<Device> findByMacs(Set<String> macs);
    List<Device> findByDeviceIds(List<DeviceId> deviceIds);
    List<Device> findByMemberId(MemberId ownerId);
}
