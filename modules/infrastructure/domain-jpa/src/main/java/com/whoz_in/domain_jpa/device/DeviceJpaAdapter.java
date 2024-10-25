package com.whoz_in.domain_jpa.device;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceJpaAdapter implements DeviceRepository {

    @Override
    public void save(Device device) {

    }
}
