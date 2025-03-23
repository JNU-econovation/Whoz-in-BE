package com.whoz_in.api_query_jpa.device.active.event;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.shared.service.DeviceConnectionService;
import com.whoz_in.api_query_jpa.shared.service.DeviceService;
import com.whoz_in.main_api.shared.domain.device.active.event.DeviceDeletedEvent;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component("ActiveDeviceDeletedEventHandler")
@RequiredArgsConstructor
public class ActiveDeviceDeletedEventHandler {

    private final ActiveDeviceRepository activeDeviceRepository;
    private final DeviceConnectionService deviceConnectionService;
    private final DeviceService deviceService;
    private final MemberConnectionInfoRepository connectionInfoRepository;


    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    @EventListener(DeviceDeletedEvent.class)
    public void handle(DeviceDeletedEvent event) {
        UUID deviceId = event.deviceId();
        ActiveDeviceEntity activeDevice =
                activeDeviceRepository.findByDeviceId(deviceId)
                                        .orElseThrow(() -> new RuntimeException("activeDevice not found"));

        UUID memberId = activeDevice.getMemberId();
        activeDeviceRepository.deleteById(event.deviceId());

        disconnectIfLastActiveDevice(activeDevice);
    }

    // 마지막 기기일 경우 memberConnectionInfo 의 continuousTime 을 병합
    private void disconnectIfLastActiveDevice(ActiveDeviceEntity ad){
        UUID ownerId = ad.getMemberId();
        UUID deviceId = ad.getDeviceId();

        LocalDateTime disconnectedAt = LocalDateTime.now();
        Optional<MemberConnectionInfo> connectionInfo = connectionInfoRepository.findByMemberId(ownerId);

        connectionInfo.ifPresent(ci -> {
            // TODO: 마지막 기기인지 검증 로직 필요
            if(ci.isActive()) {
                // 활동 중인데 삭제할 경우 and 삭제한 기기가 Active 인 마지막 기기이면 disconnect
                deviceConnectionService.disconnectDevice(deviceId, disconnectedAt);
                }
                // 활동 중이 아닌데 삭제할 경우, 부가 처리 없음
            });
    }
}
