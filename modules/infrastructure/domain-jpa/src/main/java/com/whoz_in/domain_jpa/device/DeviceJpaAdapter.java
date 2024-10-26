package com.whoz_in.domain_jpa.device;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeviceJpaAdapter implements DeviceRepository {
    private final DeviceJpaRepository repository;
    @Override
    public void save(Device device) {

    }
}
