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
import com.whoz_in.main_api.shared.caching.device.TempDeviceInfo;
import com.whoz_in.main_api.shared.caching.device.TempDeviceInfoStore;
import com.whoz_in.main_api.shared.utils.MacAddressUtil;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


// 기기 등록 전에 모든 와이파이에 대한 기기 정보가(맥) 있어야 한다.
// 이 핸들러는 하나의 기기 정보를 임시로 저장하는 역할을 한다.
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
    //TODO: 트랜잭셔널 범위 수정
    @Transactional
    @Override
    public List<String> handle(DeviceInfoTempAdd req) {
        MemberId requesterId = requesterInfo.getMemberId();
        memberFinderService.mustExist(requesterId);

        //해당 룸에서 발생한 아이피로 ManagedLog들을 찾으며, 오래된 맥일 경우 신뢰할 수 없으므로 일정 기간 이내로 찾는다.
        List<ManagedLog> managedLogs = managedLogRepository.findAllByIpLatestMac(req.ip().toString(), LocalDateTime.now().minusHours(6));

        if (managedLogs.isEmpty()) {
            throw new NoManagedLogException(req.ip().toString());
        } else if (managedLogs.size() == 1){
            // 1개면 정상
        } else if (managedLogs.size() == 2){
            // ❗️네트워크 특징 파악이 안돼서 JNU, eduroam 처리 로직 하드코딩했음
            // 2개인 경우는 jnu에 연결된 기기의 mdns 로그임
            managedLogs.removeIf(log-> log.getSsid().equals("eduroam"));
        } else {
            throw new IllegalStateException("알 수 없는 예외");
        }

        ManagedLog managedLog = managedLogs.get(0);
        String mac = managedLog.getMac();
        String room = managedLog.getRoom();

        //모니터 로그에서 현재 접속 중인 맥이 있어야 한다. (넉넉하게 15분)
        monitorLogRepository.mustExistAfter(mac, LocalDateTime.now().minusMinutes(15));

        //해당 맥으로 등록된 기기가 있으면 예외
        deviceRepository.findByMac(mac).ifPresent(d -> { //기기가 있을경우
            deviceOwnershipService.validateIsMine(d, requesterId); //내꺼 아닐때 예외
            throw DeviceAlreadyRegisteredException.EXCEPTION;//내꺼일때 예외
        });

        if (MacAddressUtil.isFixedMac(mac)){ // 고정 맥일 때
            // 모두 똑같은 맥으로 등록
            ssidConfig.getSsids().stream()
                    .map(ssid -> new TempDeviceInfo(room, ssid, mac))
                    .forEach((tdi -> tempDeviceInfoStore.add(requesterId.id(), tdi)));
            return ssidConfig.getSsids();
        } else { // 랜덤 맥일 때
            //DeviceInfo를 추가한다.
            tempDeviceInfoStore.add(requesterId.id(), new TempDeviceInfo(room, managedLog.getSsid(), mac));
            return List.of(managedLog.getSsid());
        }
    }
}
