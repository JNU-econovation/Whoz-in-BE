package com.whoz_in.api_query_jpa.device.active.event;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import com.whoz_in.api_query_jpa.shared.service.DeviceConnectionService;
import com.whoz_in.api_query_jpa.shared.service.DeviceService;
import com.whoz_in.api_query_jpa.shared.service.MemberConnectionService;
import com.whoz_in.api_query_jpa.shared.util.ActiveDeviceManager;
import com.whoz_in.api_query_jpa.shared.util.ActiveMemberConnectionManager;
import com.whoz_in.main_api.shared.domain.device.active.event.ActiveDeviceFinded;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
    private final ActiveMemberConnectionManager activeMemberConnectionManager;
    private final ActiveDeviceManager activeDeviceManager;
    private final DeviceConnectionService deviceConnectionService;
    private final MemberConnectionService memberConnectionService;
    private final DeviceService deviceService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @EventListener(ActiveDeviceFinded.class)
    public void saveActiveDevices(ActiveDeviceFinded event) {
        List<UUID> deviceIds = event.getDevices();
        List<ActiveDeviceEntity> activeDeviceEntities = activeDeviceRepository.findByDeviceIds(deviceIds);

        // TODO: 이 부분 배치로 바꿔서 순차처리 해도 될 듯

        activeDeviceEntities.stream()
                .map(ActiveDeviceEntity::getDeviceId)
                .peek(deviceConnectionService::connectDevice)
                .map(deviceService::findDeviceOwner)
                .forEach(owner -> owner.ifPresent(memberConnectionService::connectMember));

    }

}

