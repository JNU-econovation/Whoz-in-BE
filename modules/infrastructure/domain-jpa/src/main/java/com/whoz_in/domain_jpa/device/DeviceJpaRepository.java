package com.whoz_in.domain_jpa.device;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import java.util.List;
import com.whoz_in.domain.device.model.MacAddress;
import java.util.Optional;
import java.util.List;
import com.whoz_in.domain.device.model.MacAddress;
import com.whoz_in.domain.device.model.DeviceId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeviceJpaRepository implements DeviceRepository {
    private final DeviceEntityRepository repository;
    private final DeviceEntityConverter converter;
    @Override
    public void save(Device device) {
        repository.save(converter.from(device));
    }

    @Override
    public void delete(DeviceId deviceId) {
        repository.deleteById(deviceId.id());
    }

    @Override
    public List<Device> findAll() {
        return repository.findAll().stream().map(converter::to).toList();
    }

    @Override
    public Optional<Device> findByMac(String mac) {
        return repository.findByMac(mac).map(converter::to);
    }

    @Override
    public Optional<Device> findByDeviceId(DeviceId deviceId) {
        return repository.findById(deviceId.id()).map(converter::to);
    }
}
