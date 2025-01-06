package com.whoz_in.domain_jpa.device;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.MacAddress;
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
    public Optional<Device> findByMac(String mac) {
        return repository.findByMac(mac).map(converter::to);
    }
}
