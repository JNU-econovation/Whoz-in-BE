package com.whoz_in.main_api.query.device.application;

import com.whoz_in.domain.network_log.ManagedLog;
import com.whoz_in.domain.network_log.ManagedLogRepository;
import com.whoz_in.main_api.config.RoomSsidConfig;
import com.whoz_in.main_api.query.device.view.DeviceViewer;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.caching.device.TempDeviceInfo;
import com.whoz_in.main_api.shared.caching.device.TempDeviceInfoStore;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class TempDeviceInfosStatusHandler implements QueryHandler<TempDeviceInfosStatusGet, TempDeviceInfosStatus> {
    private final RoomSsidConfig ssidConfig;
    private final RequesterInfo requesterInfo;
    private final DeviceViewer deviceViewer;
    private final TempDeviceInfoStore tempDeviceInfoStore;
    private final ManagedLogRepository managedLogRepository;

    @Override
    public TempDeviceInfosStatus handle(TempDeviceInfosStatusGet query) {
        UUID requesterId = requesterInfo.getMemberId().id();

        // 사용자의 기기를 맥으로 찾기 위해 로그를 가져옴
        ManagedLog log = managedLogRepository.getLatestByRoomAndIpAfter(
                query.room(), query.ip(), LocalDateTime.now().minusDays(1)); //이거 managed log가 판단해야 함
        // 방에 존재하는 와이파이들
        List<String> roomSsids = ssidConfig.getSsids(query.room());
        // 사용자가 이전에 등록한 기기 정보를 가져옴
        List<String> registeredSsids = deviceViewer
                .findRegisteredSsids(requesterId, query.room(), log.getMac())
                .ssids();
        // 임시로 등록한 기기 정보를 가져옴
        List<TempDeviceInfo> addedTempDeviceInfos = tempDeviceInfoStore.get(requesterId);
        // 방에 존재하는 와이파이 중 이전에 등록한 와이파이를 제외하고 임시로 등록되었는지를 체크함
        Map<String, Boolean> isAddedPerSsid = roomSsids.stream()
                .filter(ssid -> !registeredSsids.contains(ssid)) //등록되지 않은 ssid만 남김
                .collect(Collectors.toMap(
                        ssid -> ssid,
                        ssid -> addedTempDeviceInfos.stream()
                                .anyMatch(tdi -> tdi.getSsid().equals(ssid))
                ));
        return new TempDeviceInfosStatus(isAddedPerSsid);
    }
}
