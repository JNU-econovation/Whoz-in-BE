package com.whoz_in.domain.device.active;

import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 구현에 따라 달라지므로 interface
 * 일반 DeviceRepository 와 역할이 너무 비슷한가?
 * ActiveDeviceRepository 의 역할
 * 1. Active 상태인 Device 들을 저장한다. (내부 구현은 동시성 문제를 해결해야 한다.)
 * 2. Active 상태인 Device 들을 조회한다.
 */
public interface ActiveDeviceRepository {

    List<Device> findAll();
    Optional<Device> findByDeviceId(DeviceId deviceId);
    boolean existsByDeviceId(DeviceId deviceId);
    void deleteByDeviceId(DeviceId deviceId);
    void save(Device device);
    void saveAll(Collection<Device> devices);
    default List<Device> getAll(){ return findAll(); }
    default Optional<Device> get(DeviceId deviceId){ return findByDeviceId(deviceId); }

}
