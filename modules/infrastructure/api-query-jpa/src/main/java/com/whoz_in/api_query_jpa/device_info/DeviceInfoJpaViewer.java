package com.whoz_in.api_query_jpa.device_info;

import com.whoz_in.api.query.device_info.application.DeviceInfo;
import com.whoz_in.api.query.device_info.application.DeviceInfoViewer;
import com.whoz_in.api.query.device_info.application.DevicesInfo;
import com.whoz_in.domain_jpa.device.DeviceEntity;
import com.whoz_in.domain_jpa.device.DeviceJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeviceInfoJpaViewer implements DeviceInfoViewer {
    private final DeviceJpaRepository deviceJpaRepository;

    @Override
    public DeviceInfo findByDeviceId(Long deviceId) {
        deviceJpaRepository.findById(deviceId);
        deviceJpaRepository.save(new DeviceEntity());
        return new DeviceInfo();
    }

    @Override
    public DevicesInfo findAll() {
        List<DeviceEntity> devices = deviceJpaRepository.findAll();
        return new DevicesInfo();
    }
}
