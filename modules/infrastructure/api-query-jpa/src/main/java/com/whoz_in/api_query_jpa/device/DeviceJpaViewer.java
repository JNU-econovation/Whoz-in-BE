package com.whoz_in.api_query_jpa.device;

import com.whoz_in.main_api.query.device.application.DevicesStatus;
import com.whoz_in.main_api.query.device.application.DevicesStatus.DeviceStatus;
import com.whoz_in.main_api.query.device.view.DeviceViewer;
import com.whoz_in.main_api.query.device.view.RegisteredSsids;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DeviceJpaViewer implements DeviceViewer {
    private final DeviceRepository deviceRepository;
    private final DeviceInfoRepository deviceInfoRepository;
    @Override
    public RegisteredSsids findRegisteredSsids(UUID ownerId, String room, String mac) {
        return new RegisteredSsids(
                deviceInfoRepository.findAllByMac(ownerId, room, mac).stream()
                        .map(DeviceInfo::getSsid)
                        .toList());
    }

    @Override
    public DevicesStatus findDevicesStatus(UUID ownerId) {
        List<DeviceStatus> devicesStatus = deviceRepository.findAllByMemberId(ownerId)
                .stream()
                .map(device -> new DeviceStatus(device.getId(), device.getDeviceInfos()
                        .stream()
                        .collect(Collectors.toMap(
                                DeviceInfo::getSsid,
                                DeviceInfo::getMac)),
                        null))//TODO: connected된 ssid는 기기 현황 구현 후 리팩토링하면서 구현하기
                .toList();
        return new DevicesStatus(devicesStatus);
    }
}
