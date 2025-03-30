package com.whoz_in.main_api.command.device.application;

import static com.whoz_in.main_api.command.device.application.AdditionStatus.ADDED;
import static com.whoz_in.main_api.command.device.application.AdditionStatus.MULTIPLE_CANDIDATES;

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
import com.whoz_in.main_api.shared.jwt.tokens.DeviceRegisterToken;
import com.whoz_in.main_api.shared.utils.MacAddressUtil;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

// 기기 등록 전에 모든 와이파이에 대한 기기 정보가(맥) 있어야 한다.
// 이 핸들러는 하나의 기기 정보를 임시로 저장하는 역할을 한다.
@Slf4j
@Handler
@RequiredArgsConstructor
public class DeviceInfoTempAddHandler implements CommandHandler<DeviceInfoTempAdd, DeviceInfoTempAddRes> {
    private static final Map<MemberId, Object> locks = new ConcurrentHashMap<>();
    private static final Map<MemberId, DeviceRegisterToken> lastUsedTokens = new ConcurrentHashMap<>();
    private final RequesterInfo requesterInfo;
    private final TempDeviceInfoStore tempDeviceInfoStore;
    private final MemberFinderService memberFinderService;
    private final DeviceRepository deviceRepository;
    private final DeviceOwnershipService deviceOwnershipService;
    private final ManagedLogRepository managedLogRepository;
    private final MonitorLogRepository monitorLogRepository;
    private final RoomSsidConfig ssidConfig;

    //연결 시마다 맥이 바뀌는 기기가 다시 똑같은 와이파이에 등록하려고 하는 경우는 막지 못함
    @Override
    public DeviceInfoTempAddRes handle(DeviceInfoTempAdd req) {
        MemberId requesterId = requesterInfo.getMemberId();
        Object lock = locks.computeIfAbsent(requesterId, k -> new Object());

        synchronized (lock) {
            DeviceRegisterToken prev = lastUsedTokens.get(requesterId);

            // 새로운 토큰으로 맥 등록을 시작하는 경우 이전에 등록된 것들을 초기화함
            if (prev == null || !prev.equals(req.token())) {
                tempDeviceInfoStore.remove(requesterId.id());
                lastUsedTokens.put(requesterId, req.token());
            }

            return doHandle(req, requesterId);
        }
    }

    // 기기가 jnu나 eduroam에 연결하면 둘 다 log가 뜨는 현상이 종종 발생함. (mdns repeating)
    // 이 경우 어떤 ssid인지 알 수 없으므로 적절하게 처리해야 하는데,
    // 사용자의 개입을 최대한 줄이려고 하다가 복잡한 로직이 됐습니다... 죄송합니다...
    @Transactional(readOnly = true)
    protected DeviceInfoTempAddRes doHandle(DeviceInfoTempAdd req, MemberId requesterId){
        memberFinderService.mustExist(requesterId);

        //해당 룸에서 발생한 아이피로 ManagedLog들을 찾으며, 오래된 맥일 경우 신뢰할 수 없으므로 일정 기간 이내로 찾는다.
        List<ManagedLog> managedLogs = managedLogRepository.findAllByIpLatestMac(req.ip().toString(), LocalDateTime.now().minusHours(3));

        ManagedLog managedLog;

        if (managedLogs.isEmpty()) { //로그가 없을때
            throw new NoManagedLogException(req.ip().toString());
        } else if (managedLogs.size() == 1) { // 로그가 하나일 때
            managedLog = managedLogs.get(0);
        } else { // 로그가 2개 이상일 때
            ManagedLog[] logs = managedLogs.stream()
                    .sorted(Comparator.comparing(ManagedLog::getUpdatedAt).reversed())
                    .limit(2)
                    .toArray(ManagedLog[]::new);
            ManagedLog newest = logs[0];
            ManagedLog secondNewest = logs[1];
            Duration duration = Duration.between(secondNewest.getUpdatedAt(), newest.getUpdatedAt());

            // 1시간 이상 차이나면 2개가 뜨다가 1개만 뜨게 된 것이라고 판단하여 나중에 뜬 것을 고른다.
            if (duration.toHours() >= 1){
                managedLog = newest;
            } else {
                log.info("[로그 2개 이상] ip: {}, ssidHint:{}", req.ip(), req.ssidHint());
                if (MacAddressUtil.isFixedMac(managedLogs.get(0).getMac())) { // 고정맥인 경우 바로 넘어감
                    log.info("[로그 2개 이상] 고정 mac이라 넘어감 : {}", managedLogs.get(0).getMac());
                    managedLog = managedLogs.get(0);
                } else { // 랜덤맥인 경우
                    // 요청의 ssid와 맞는 log를 찾기
                    Optional<ManagedLog> matched = Optional.ofNullable(req.ssidHint())
                            .flatMap(ssid ->
                                    managedLogs.stream()
                                            .filter(ml -> ml.getSsid().equals(ssid))
                                            .findAny()
                            );
                    if (matched.isPresent()) { // 요청의 ssidHint와 맞는 log가 있는경우
                        // ssidHint를 완전 믿을 수 없음. 그러므로 등록이 안된건지 확인
                        Optional<ManagedLog> any = matched
                                .filter(ml -> !tempDeviceInfoStore.existsBySsid(requesterId.id(),
                                        ml.getSsid()))
                                .filter(ml -> !tempDeviceInfoStore.existsByMac(requesterId.id(),
                                        ml.getMac()));
                        log.info("로그 2개 이상] ssidHint와 맞는 ssid인 로그 존재: {}", any.isPresent());
                        if (any.isEmpty()) {
                            return new DeviceInfoTempAddRes(ADDED, addedSsids(requesterId));
                        }
                        managedLog = any.get();
                    } else { // 없는경우
                        // 등록되지 않은 로그로 필터링
                        List<ManagedLog> notRegistered = managedLogs.stream()
                                .filter(ml -> !tempDeviceInfoStore.existsBySsid(requesterId.id(),
                                        ml.getSsid()))
                                .filter(ml -> !tempDeviceInfoStore.existsByMac(requesterId.id(),
                                        ml.getMac()))
                                .toList();
                        if (notRegistered.isEmpty()) { // 등록되지 않은게 없으면 리턴
                            log.info("[로그 2개 이상] 이미 모든 로그는 등록되었음");
                            return new DeviceInfoTempAddRes(ADDED, addedSsids(requesterId));
                        }
                        if (notRegistered.size() == 1) { // 등록되지 않은게 하나면 바로 진행
                            log.info("[로그 2개 이상] 등록되지 않은 맥 하나 존재");
                            managedLog = notRegistered.get(0);
                        } else { // 2개 이상이면 - 고를 수 없다는 응답 보내기
                            log.info("[로그 2개 이상] 등록되지 않은 맥이 2개 이상 존재");
                            return new DeviceInfoTempAddRes(
                                    MULTIPLE_CANDIDATES,
                                    notRegistered.stream()
                                            .map(ManagedLog::getSsid)
                                            .toList());
                        }
                    }
                }
            }
        }

        String mac = managedLog.getMac();
        String room = managedLog.getRoom();

        validateNotRegisteredDevice(requesterId, mac);

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
                return new DeviceInfoTempAddRes(ADDED, addedSsids(requesterId));
            }
        }

        // 등록하려는 맥이 랜덤 맥일 때 / 고정 맥이더라도 이미 등록된 TempDeviceInfo 중 랜덤 맥이 있을때
        // DeviceInfo를 추가한다.
        tempDeviceInfoStore.add(requesterId.id(), new TempDeviceInfo(room, ssid, mac));
        return new DeviceInfoTempAddRes(ADDED, addedSsids(requesterId));
    }

    //해당 맥으로 등록된 기기가 없는지 검증
    private void validateNotRegisteredDevice(MemberId requesterId, String mac){
        deviceRepository.findByMac(mac).ifPresent(d -> { //기기가 있을경우
            deviceOwnershipService.validateIsMine(d, requesterId); //내꺼 아닐때 예외
            throw DeviceAlreadyRegisteredException.EXCEPTION;//내꺼일때 예외
        });
    }
    private List<String> addedSsids(MemberId ownerId){
        return tempDeviceInfoStore.get(ownerId.id()).stream().map(TempDeviceInfo::getSsid).toList();
    }
}
