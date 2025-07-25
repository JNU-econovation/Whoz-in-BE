package com.whoz_in_infra.infra_jpa.query.device;

import com.whoz_in.main_api.query.device.application.DeviceCount;
import com.whoz_in.main_api.query.device.application.DevicesStatus;
import com.whoz_in.main_api.query.device.application.DevicesStatus.DeviceStatus;
import com.whoz_in.main_api.query.device.application.DeviceViewer;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DeviceJpaViewer implements DeviceViewer {
    private final DeviceRepository deviceRepository;

    @Override
    public DevicesStatus findDevicesStatus(UUID ownerId) {
        List<DeviceStatus> devicesStatus = deviceRepository.findAllByMemberId(ownerId)
                .stream()
                .map(device -> new DeviceStatus(device.getId(), device.getName(),
                        device.getDeviceInfos().stream()
                            .collect(Collectors.toMap(
                                    DeviceInfo::getSsid,
                                    DeviceInfo::getMac)),
                        null))//TODO: connected된 ssid는 기기 현황 구현 후 리팩토링하면서 구현하기
                .toList();
        return new DevicesStatus(devicesStatus);
    }

    @Override
    public DeviceCount countDevice(UUID ownerId) {
        List<Device> devices = deviceRepository.findAllByMemberId(ownerId);
        return new DeviceCount(devices.size());
    }
}
