package com.whoz_in.api_query_jpa.device;

import com.whoz_in.main_api.query.device.view.DeviceInfoViewer;
import com.whoz_in.main_api.query.device.view.RegisteredSsids;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DeviceInfoJpaViewer implements DeviceInfoViewer {
    private final DeviceInfoRepository deviceInfoRepository;
    @Override
    public RegisteredSsids findRegisteredSsids(UUID ownerId, String room, String mac) {
        return new RegisteredSsids(
                deviceInfoRepository.findAllByMac(ownerId, room, mac).stream()
                        .map(DeviceInfo::getSsid)
                        .toList());
    }
}
