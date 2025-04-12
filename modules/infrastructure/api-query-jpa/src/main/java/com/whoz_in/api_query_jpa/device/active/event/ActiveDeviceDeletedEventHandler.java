package com.whoz_in.api_query_jpa.device.active.event;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.shared.service.DeviceConnectionService;
import com.whoz_in.shared.domain_event.device.DeviceDeleted;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Slf4j
public class ActiveDeviceDeletedEventHandler {

    private final ActiveDeviceRepository activeDeviceRepository;
    private final DeviceConnectionService deviceConnectionService;
    private final MemberConnectionInfoRepository connectionInfoRepository;


    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    @TransactionalEventListener(DeviceDeleted.class)
    public void handle(DeviceDeleted event) {
        log.info("[DeviceDeletedEvent] ActiveDevice 삭제 (deviceId) : {}", event.getDeviceId());
        UUID deviceId = event.getDeviceId();
        ActiveDeviceEntity activeDevice =
                activeDeviceRepository.findByDeviceId(deviceId)
                        .orElseThrow(()-> new IllegalArgumentException("active device not found"));

        disconnectIfLastActiveDevice(activeDevice);
        activeDeviceRepository.deleteById(event.getDeviceId());


    }

    // 마지막 기기일 경우 memberConnectionInfo 의 continuousTime 을 병합
    private void disconnectIfLastActiveDevice(ActiveDeviceEntity ad){
        UUID ownerId = ad.getMemberId();
        UUID deviceId = ad.getDeviceId();

        LocalDateTime disconnectedAt = LocalDateTime.now();
        Optional<MemberConnectionInfo> connectionInfo = connectionInfoRepository.findByMemberId(ownerId);

        connectionInfo.ifPresent(ci -> {
            if(ci.isActive()) {
                // 활동 중인데 삭제할 경우 and 삭제한 기기가 Active 인 마지막 기기이면 disconnect
                deviceConnectionService.disconnectDevice(deviceId, disconnectedAt);
                }
                // 활동 중이 아닌데 삭제할 경우, 부가 처리 없음
            });
    }
}
