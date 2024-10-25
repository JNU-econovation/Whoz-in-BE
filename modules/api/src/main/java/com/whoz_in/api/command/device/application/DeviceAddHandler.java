package com.whoz_in.api.command.device.application;

import com.whoz_in.api.shared.application.Handler;
import com.whoz_in.api.shared.application.command.CommandHandler;
import com.whoz_in.api.shared.utils.RequesterInfo;
import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.shared.event.EventBus;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public final class DeviceAddHandler extends CommandHandler<DeviceAdd> {
    private final RequesterInfo requesterInfo;
    private final DeviceRepository repository;
    private final EventBus eventBus;
    @Override
    public void handle(DeviceAdd command) {
        Device device = Device.create(
                requesterInfo.getUserId(), command.getMacAddress(), command.getIpAddress());
        repository.save(device);
        eventBus.publish(device.pullDomainEvents());
    }
}
