package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceInfo;
import com.whoz_in.domain.device.model.MacAddress;
import com.whoz_in.domain.device.service.DeviceFinderService;
import com.whoz_in.domain.member.service.MemberFinderService;
import com.whoz_in.domain.network_log.ManagedLog;
import com.whoz_in.domain.network_log.ManagedLogRepository;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;


// 기기 등록이 완료된 기기의 기기 정보를 수정한다.
@Handler
@RequiredArgsConstructor
public class DeviceInfoEditHandler implements CommandHandler<DeviceInfoEdit, Void> {
    private final RequesterInfo requesterInfo;
    private final MemberFinderService memberFinderService;
    private final DeviceFinderService deviceFinderService;
    private final DeviceRepository deviceRepository;
    private final MonitorLogRepository monitorLogRepository;
    private final ManagedLogRepository managedLogRepository;

    @Override
    public Void handle(DeviceInfoEdit command) {
        memberFinderService.mustExist(requesterInfo.getMemberId());
        deviceFinderService.mustNotExistByMac(command.newMac());

        //유효한 mac인지 확인
        ManagedLog managedLog = managedLogRepository.getBySsidAndMac(command.ssid(), command.newMac());
        monitorLogRepository.mustExistAfter(command.newMac(), LocalDateTime.now().minusMinutes(15)); //TODO: network log에서 제공하는 기능으로

        //device info 최신화
        Device device = deviceFinderService.find(command.getDeviceId());
        device.updateDeviceInfo(
                DeviceInfo.create(
                        managedLog.getRoom(), //방이 1개라는 기준으로 처리한거. 2개 이상이면 Command로부터 가져와야 함
                        managedLog.getSsid(),
                        MacAddress.create(managedLog.getMac()))
        );
        deviceRepository.save(device);
        return null;
    }
}
