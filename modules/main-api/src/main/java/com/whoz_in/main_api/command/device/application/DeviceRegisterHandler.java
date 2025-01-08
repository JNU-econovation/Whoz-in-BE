package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceInfo;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.service.MemberFinderService;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class DeviceRegisterHandler implements CommandHandler<DeviceRegister, Void> {
    private final RequesterInfo requesterInfo;
    private final DeviceInfoStore deviceInfoStore;
    private final MemberFinderService memberFinderService;
    private final DeviceRepository deviceRepository;

    @Transactional
    @Override
    public Void handle(DeviceRegister cmd) {
        MemberId requesterId = requesterInfo.getMemberId();
        memberFinderService.mustExist(requesterId);

        deviceInfoStore.verifyAllAdded(requesterId, cmd.room());

        List<DeviceInfo> deviceInfos = deviceInfoStore.takeout(requesterId);

        Device device = Device.create(requesterId, deviceInfos, cmd.room());
        deviceRepository.save(device);
        return null;
    }
}
