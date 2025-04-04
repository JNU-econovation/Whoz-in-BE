package com.whoz_in.api_query_jpa.device.active.event;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import com.whoz_in.api_query_jpa.shared.service.DeviceConnectionService;
import com.whoz_in.api_query_jpa.shared.service.DeviceService;
import com.whoz_in.api_query_jpa.shared.service.MemberConnectionService;
import com.whoz_in.main_api.shared.domain.device.active.event.ActiveDeviceFound;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActiveDeviceEventHandler {

    private final ActiveDeviceRepository activeDeviceRepository;
    private final MemberRepository memberRepository;
    private final DeviceConnectionService deviceConnectionService;
    private final MemberConnectionService memberConnectionService;
    private final DeviceService deviceService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @EventListener(ActiveDeviceFound.class)
    public void saveActiveDevices(ActiveDeviceFound event) {
        List<UUID> deviceIds = event.devices();
        List<ActiveDeviceEntity> activeDeviceEntities = activeDeviceRepository.findByDeviceIds(deviceIds);

//        LocalDateTime connectedAt = LocalDateTime.now();
        // TODO: 이 부분 배치로 바꿔서 일괄처리 해도 될 듯

        activeDeviceEntities
                .forEach(ad -> {
                        UUID deviceId = ad.getDeviceId();
                        UUID memberId = ad.getMemberId();
                        LocalDateTime connectedAt = deviceService.findLatestMonitorLogAt(deviceId).getUpdatedAt();
                        deviceConnectionService.connectDevice(deviceId, connectedAt);
                        memberConnectionService.connectMember(memberId, connectedAt);
                    });

    }

}
