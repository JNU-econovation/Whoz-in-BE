package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.exception.DeviceAlreadyRegisteredException;
import com.whoz_in.domain.device.model.Device;
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
import java.util.Optional;
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

//        managedLogs.removeIf(log ->
//                tempDeviceInfoStore.exists(requesterId.id(), log.getSsid())
//        );
        if (managedLogs.isEmpty()) throw new NoManagedLogException(req.ip().toString());

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
            List<TempDeviceInfo> storedDIs = tempDeviceInfoStore.get(requesterId.id());
            boolean isStoredMac = storedDIs.stream()
                    .anyMatch(tdi -> tdi.getMac().equals(mac));
            // 랜덤 맥은 ssid마다 맥이 다를 수 밖에 없기 때문에 같은걸 등록하려는 경우 리턴
            if (isStoredMac) return List.of();
            // 이미 등록된 맥들의 ssid들을 가져옴
            List<String> storedSsids = storedDIs.stream()
                    .map(TempDeviceInfo::getSsid).toList();
            // 아직 등록이 안된 ssid의 로그를 가져옴 (맥이 등록 안된 경우 등록 안된 ssid가 없을리가 없음. 이 경우 맥이 바뀐거일텐데 불가능하다고 봄)
            String notStoredSsid = managedLogs.stream()
                    .map(ManagedLog::getSsid)
                    .filter(ssid -> !storedSsids.contains(ssid))
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException(storedSsids + ", " + mac + " - mdns 로그 바뀜"));
            //DeviceInfo를 추가한다.
            tempDeviceInfoStore.add(requesterId.id(), new TempDeviceInfo(room, notStoredSsid, mac));
            return List.of(notStoredSsid);
        }
    }
}
