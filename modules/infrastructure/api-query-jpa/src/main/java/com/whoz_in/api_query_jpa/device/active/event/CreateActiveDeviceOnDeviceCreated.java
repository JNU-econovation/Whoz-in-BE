package com.whoz_in.api_query_jpa.device.active.event;



import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import com.whoz_in.main_api.shared.domain.device.active.event.ActiveDeviceFound;
import com.whoz_in.main_api.shared.event.Events;
import com.whoz_in.shared.domain_event.device.DeviceCreated;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateActiveDeviceOnDeviceCreated {
    private final ActiveDeviceRepository activeDeviceRepository;
    private final MemberRepository memberRepository;

    @TransactionalEventListener(DeviceCreated.class)
    @Transactional(propagation = REQUIRES_NEW)
    public void handle(DeviceCreated event) {
        log.info("[DeviceCreatedEvent] ActiveDevice 생성 (deviceId) : {}", event.getDeviceId());
        UUID deviceId = event.getDeviceId();
        UUID memberId = memberRepository.getByDeviceId(deviceId).getId();

        ActiveDeviceEntity activeDevice = ActiveDeviceEntity.create(deviceId, memberId);

        activeDeviceRepository.save(activeDevice);
        Events.raise(new ActiveDeviceFound(List.of(deviceId)));
    }

}
