package com.whoz_in.infra.jpa.device;

import com.whoz_in.domain.device.domain.DeviceRepository;
import com.whoz_in.domain.device.domain.model.Device;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceJpaAdapter implements DeviceRepository {

    @Override
    public void save(Device device) {

    }
}
