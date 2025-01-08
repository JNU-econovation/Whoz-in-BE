package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.DeviceInfo;
import com.whoz_in.domain.device.model.MacAddress;
import com.whoz_in.domain.device.service.DeviceOwnershipService;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.service.MemberFinderService;
import com.whoz_in.domain.network_log.ManagedLog;
import com.whoz_in.domain.network_log.ManagedLogRepository;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class DeviceInfoAddHandler implements CommandHandler<DeviceInfoAdd, Void> {
    private final RequesterInfo requesterInfo;
    private final DeviceInfoStore deviceInfoStore;
    private final MemberFinderService memberFinderService;
    private final DeviceOwnershipService deviceOwnershipService;
    private final DeviceRepository deviceRepository;
    //얘네 도메인에서의 입지가 애매해서 일단 Repository로 다뤘습니다.
    private final ManagedLogRepository managedLogRepository;
    private final MonitorLogRepository monitorLogRepository;

    //연결 시마다 맥이 바뀌는 기기가 다시 똑같은 와이파이에 등록하려고 하는 경우는 막지 못함
    @Transactional
    @Override
    public Void handle(DeviceInfoAdd req) {
        MemberId requesterId = requesterInfo.getMemberId();
        memberFinderService.mustExist(requesterId);

        //해당 룸에서 발생한 아이피로 ManagedLog를 찾으며, 오래된 맥일 경우 신뢰할 수 없으므로 무시한다.
        ManagedLog managedLog = managedLogRepository.getLatestByRoomAndIpAfter(req.room(), req.ip().toString(), LocalDateTime.now().minusDays(1));

        String mac = managedLog.getMac();

        //등록할 DeviceInfo 생성
        DeviceInfo deviceInfo = DeviceInfo.create(req.room(), managedLog.getSsid(), MacAddress.create(mac));
        //이미 등록된 DeviceInfo가 아닌지 미리 확인
        deviceInfoStore.mustNotExist(requesterId, deviceInfo);

        //모니터 로그에서 시간 내에 존재하는 맥이 있는지 확인
        monitorLogRepository.mustExistAfter(mac, LocalDateTime.now().minusDays(1));

        //해당 맥으로 이미 등록된 기기가 있을경우
        deviceRepository.findByMac(mac).ifPresent(device -> {
            deviceOwnershipService.validateOwnership(device, requesterId);  // 회원에게 기기 삭제를 부탁하세요.라는 말을 해야 되는데..
            //자신의 것이면
            throw new IllegalArgumentException("이미 등록된 기기입니다.");
        });

        //마침내! DeviceInfo를 추가한다.
        deviceInfoStore.add(requesterId, deviceInfo);

        return null;
    }
}
