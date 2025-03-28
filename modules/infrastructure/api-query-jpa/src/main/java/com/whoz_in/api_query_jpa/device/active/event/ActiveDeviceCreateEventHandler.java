package com.whoz_in.api_query_jpa.device.active.event;


import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import com.whoz_in.main_api.shared.domain.device.active.event.DeviceCreatedEvent;
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
public class ActiveDeviceCreateEventHandler {

    private final ActiveDeviceRepository activeDeviceRepository;
    private final MemberRepository memberRepository;

    @EventListener(DeviceCreatedEvent.class)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void handle(DeviceCreatedEvent event) {
        log.info("[DeviceCreatedEvent] ActiveDevice 생성 (deviceId) : {}", event.deviceId());
        UUID deviceId = event.deviceId();
        UUID memberId = memberRepository.getByDeviceId(deviceId).getId();

        ActiveDeviceEntity activeDevice = ActiveDeviceEntity.create(deviceId, memberId);

        activeDeviceRepository.save(activeDevice);
    }

}
