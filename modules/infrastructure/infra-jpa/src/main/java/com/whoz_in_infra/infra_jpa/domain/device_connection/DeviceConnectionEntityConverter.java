package com.whoz_in_infra.infra_jpa.domain.device_connection;

import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.device_connection.DeviceConnection;
import com.whoz_in.domain.device_connection.DeviceConnectionId;
import com.whoz_in_infra.infra_jpa.domain.shared.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class DeviceConnectionEntityConverter extends BaseConverter<DeviceConnectionEntity, DeviceConnection> {

    @Override
    public DeviceConnectionEntity from(DeviceConnection deviceConnection) {
        return new DeviceConnectionEntity(
                deviceConnection.getId().id(),
                deviceConnection.getDeviceId().id(),
                deviceConnection.getRoom(),
                deviceConnection.getConnectedAt(),
                deviceConnection.getDisconnectedAt()
        );
    }

    @Override
    public DeviceConnection to(DeviceConnectionEntity entity) {
        return DeviceConnection.load(
                new DeviceConnectionId(entity.getId()),
                new DeviceId(entity.getDeviceId()),
                entity.getRoom(),
                entity.getConnectedAt(),
                entity.getDisconnectedAt()
        );
    }
}
