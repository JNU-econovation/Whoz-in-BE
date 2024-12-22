package com.whoz_in.main_api.command.device.application;

import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.application.command.CommandHandler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.shared.event.EventBus;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class DeviceAddHandler extends CommandHandler<DeviceAdd> {
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
