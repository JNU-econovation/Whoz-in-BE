package com.whoz_in_infra.infra_jpa.domain.device;

import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.device.model.DeviceInfo;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in_infra.infra_jpa.domain.shared.BaseConverter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class DeviceEntityConverter extends BaseConverter<DeviceEntity, Device> {
    @Override
    public DeviceEntity from(Device device) {
        List<DeviceInfoEntity> deviceInfoEntities = device.getDeviceInfos().stream()
                .map(deviceInfo -> DeviceInfoEntity.builder()
                        .room(deviceInfo.getRoom())
                        .ssid(deviceInfo.getSsid())
                        .mac(deviceInfo.getMac().toString())
                        .build())
                .toList();
        return DeviceEntity.builder()
                .id(device.getId().id())
                .memberId(device.getMemberId().id())
                .name(device.getDeviceName())
                .deviceInfoEntity(deviceInfoEntities)
                .deletedAt(device.getDeactivatedAt())
                .build();
    }

    @Override
    public Device to(DeviceEntity entity) {
        return Device.load(
                new DeviceId(entity.getId()),
                new MemberId(entity.getMemberId()),
                entity.getDeviceInfoEntity().stream()
                        .map(deviceInfoEntity -> DeviceInfo.load(
                                deviceInfoEntity.getRoom(),
                                deviceInfoEntity.getSsid(),
                                deviceInfoEntity.getMac()))
                        .collect(Collectors.toSet()),
                entity.getName(),
                entity.getDeletedAt()
        );
    }
}
