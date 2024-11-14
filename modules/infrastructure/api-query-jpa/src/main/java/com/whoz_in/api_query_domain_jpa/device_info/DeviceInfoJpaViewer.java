package com.whoz_in.api_query_domain_jpa.device_info;

import com.whoz_in.api.query.device_info.application.DeviceInfo;
import com.whoz_in.api.query.device_info.application.DeviceInfoViewer;
import com.whoz_in.api.query.device_info.application.DevicesInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeviceInfoJpaViewer implements DeviceInfoViewer {

    @Override
    public DeviceInfo findByDeviceId(Long deviceId) {
        return new DeviceInfo();
    }

    @Override
    public DevicesInfo findAll() {

        return new DevicesInfo();
    }
}
