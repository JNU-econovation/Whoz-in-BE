package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.exception.DeviceAlreadyRegisteredException;
import com.whoz_in.domain.device.service.DeviceOwnershipService;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.service.MemberFinderService;
import com.whoz_in.domain.network_log.ManagedLog;
import com.whoz_in.domain.network_log.ManagedLogRepository;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.domain.network_log.NoManagedLogException;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.config.RoomSsidConfig;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.application.caching.device.TempDeviceInfo;
import com.whoz_in.main_api.shared.application.caching.device.TempDeviceInfoStore;
import com.whoz_in.main_api.shared.utils.MacAddressUtil;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


// 기기 등록 전에 모든 와이파이에 대한 기기 정보가(맥) 있어야 한다.
// 이 핸들러는 하나의 기기 정보를 임시로 저장하는 역할을 한다.
@Slf4j
@Handler
@RequiredArgsConstructor
public class DeviceInfoTempAddHandler implements CommandHandler<DeviceInfoTempAdd, List<String>> {
    private final RequesterInfo requesterInfo;
    private final TempDeviceInfoStore tempDeviceInfoStore;
    private final MemberFinderService memberFinderService;
    private final DeviceRepository deviceRepository;
    private final DeviceOwnershipService deviceOwnershipService;
    //얘네 도메인에서의 입지가 애매해서 일단 Repository로 다뤘습니다.
    private final ManagedLogRepository managedLogRepository;
    private final MonitorLogRepository monitorLogRepository;
    private final RoomSsidConfig ssidConfig;

    //연결 시마다 맥이 바뀌는 기기가 다시 똑같은 와이파이에 등록하려고 하는 경우는 막지 못함
    @Transactional
    @Override
    public List<String> handle(DeviceInfoTempAdd req) {
        MemberId requesterId = requesterInfo.getMemberId();
        memberFinderService.mustExist(requesterId);

        //해당 룸에서 발생한 아이피로 ManagedLog들을 찾으며, 오래된 맥일 경우 신뢰할 수 없으므로 일정 기간 이내로 찾는다./ma
        List<ManagedLog> managedLogs = managedLogRepository.findAllByIpLatestMac(req.ip().toString(), LocalDateTime.now().minusMinutes(10));

        ManagedLog managedLog;

        if (managedLogs.isEmpty()) {
            throw new NoManagedLogException(req.ip().toString());
        } else if (managedLogs.size() == 1){
            managedLog = managedLogs.get(0);
        } else {
            ManagedLog[] logs = managedLogs.stream()
                    .sorted(Comparator.comparing(ManagedLog::getCreatedAt))
                    .limit(2)
                    .toArray(ManagedLog[]::new);

            // 기기가 jnu나 eduroam에 연결하면 둘 다 log가 뜨는 현상이 종종 발생함.
            ManagedLog oldest = logs[0];
            ManagedLog secondOldest = logs[1];

            Duration duration = Duration.between(oldest.getCreatedAt(), secondOldest.getCreatedAt());

            // 10초 이상 차이나면 올바른 1개만 뜨다가 2개가 뜬 경우이기 때문에 먼저 뜬 것을 고름
            // 10초 미만은 처음부터 2개가 뜨는 경우라고 판단하여 오류를 발생
            if (duration.toSeconds() <= 10) throw new DeviceInfoTempAddFailedException(req.ip().toString());
            managedLog = oldest;
        }

        String mac = managedLog.getMac();
        String room = managedLog.getRoom();

        //해당 맥으로 등록된 기기가 있으면 예외
        deviceRepository.findByMac(mac).ifPresent(d -> { //기기가 있을경우
            deviceOwnershipService.validateIsMine(d, requesterId); //내꺼 아닐때 예외
            throw DeviceAlreadyRegisteredException.EXCEPTION;//내꺼일때 예외
        });

        //모니터 로그에서 현재 접속 중인 맥이 있어야 한다.
        monitorLogRepository.mustExistAfter(mac, LocalDateTime.now().minusHours(3));

        String ssid = managedLog.getSsid();
        if (MacAddressUtil.isFixedMac(mac)){ // 고정 맥일 때
            boolean isRandomMacExisting = tempDeviceInfoStore.get(requesterId.id()).stream()
                    .anyMatch(tdi -> !MacAddressUtil.isFixedMac(tdi.getMac()));
            if (!isRandomMacExisting){
                // 모두 똑같은 맥으로 등록
                ssidConfig.getSsids().stream()
                        .map(storedSsid -> new TempDeviceInfo(room, storedSsid, mac))
                        .forEach((tdi -> tempDeviceInfoStore.add(requesterId.id(), tdi)));
                return ssidConfig.getSsids();
            }
        }

        // 등록하려는 맥이 랜덤 맥일 때 / 고정 맥이더라도 이미 등록된 TempDeviceInfo 중 랜덤 맥이 있을때
        // DeviceInfo를 추가한다.
        tempDeviceInfoStore.add(requesterId.id(), new TempDeviceInfo(room, ssid, mac));
        return List.of(ssid);
    }
}
