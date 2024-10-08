package com.whoz_in.infra.jpa.device;

import com.whoz_in.domain.device.domain.Device;
import com.whoz_in.domain.device.domain.DeviceRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceJpaAdapter implements DeviceRepository {

    @Override
    public void save(Device device) {

    }
}
