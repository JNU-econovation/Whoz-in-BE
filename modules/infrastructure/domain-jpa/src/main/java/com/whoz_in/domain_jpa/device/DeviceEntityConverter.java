package com.whoz_in.domain_jpa.device;

import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.device.model.DeviceInfo;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain_jpa.shared.BaseConverter;
import java.util.List;
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
        return new DeviceEntity(device.getId().id(), device.getMemberId().id(), device.getName(), deviceInfoEntities);
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
                        .toList(),
                entity.getName()
        );
    }
}
