package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.service.DeviceFinderService;
import com.whoz_in.domain.member.service.MemberFinderService;
import com.whoz_in.domain.shared.event.EventBus;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class DeviceRemoveHandler implements CommandHandler<DeviceRemove, Void> {
    private final RequesterInfo requesterInfo;
    private final MemberFinderService memberFinderService;
    private final DeviceRepository deviceRepository;
    private final DeviceFinderService deviceFinderService;
    private final EventBus eventBus;

    @Transactional
    @Override
    public Void handle(DeviceRemove command) {
        memberFinderService.mustExist(requesterInfo.getMemberId());
        Device device = deviceFinderService.find(command.getDeviceId());
        device.deactivate();
        deviceRepository.save(device);
        eventBus.publish(device.pullDomainEvents());
        return null;
    }
}
