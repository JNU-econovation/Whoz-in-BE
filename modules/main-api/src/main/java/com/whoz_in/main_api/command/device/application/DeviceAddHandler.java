package com.whoz_in.main_api.command.device.application;

import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.shared.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class DeviceAddHandler implements CommandHandler<DeviceAdd> {
    private final RequesterInfo requesterInfo;
    private final DeviceRepository repository;
    private final EventBus eventBus;
    @Transactional
    @Override
    public void handle(DeviceAdd command) {
        Device device = Device.create(
                requesterInfo.getUserId(), command.getMacAddress(), command.getIpAddress(), command.getDeviceName());
        repository.save(device);
        eventBus.publish(device.pullDomainEvents());
    }
}
