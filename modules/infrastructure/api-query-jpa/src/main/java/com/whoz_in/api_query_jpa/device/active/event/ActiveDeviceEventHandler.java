package com.whoz_in.api_query_jpa.device.active.event;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
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
    private final MemberConnectionInfoRepository connectionInfoRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @EventListener(ActiveDeviceFinded.class)
    public void saveActiveDevices(ActiveDeviceFinded event) {
        List<UUID> deviceIds = event.getDevices();
        List<ActiveDeviceEntity> activeDeviceEntities = activeDeviceRepository.findAll();

        List<ActiveDeviceEntity> firstActiveDevices = findFirstActiveDevices(deviceIds);
        List<ActiveDeviceEntity> nonFirstActiveDevice = findNonFirstActiveDevices(deviceIds);

        save(firstActiveDevices);
        save(nonFirstActiveDevice);

        activeOn(firstActiveDevices);
        activeOn(nonFirstActiveDevice);
    }

    private List<ActiveDeviceEntity> findFirstActiveDevices(List<UUID> deviceIds){
        return deviceIds.stream()
                .filter(deviceId -> !activeDeviceRepository.existsByDeviceId(deviceId))
                .map(ActiveDeviceEntity::create)
                .toList();
    }

    private List<ActiveDeviceEntity> findNonFirstActiveDevices(List<UUID> deviceIds){
        return deviceIds.stream()
                .map(activeDeviceRepository::findByDeviceId)
                .map(opt -> opt.orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    private void save(List<ActiveDeviceEntity> entities){
        entities.forEach(activeDevice -> activeDevice.connect(LocalDateTime.now()));
        activeDeviceRepository.saveAll(entities);
    }

    private void activeOn(List<ActiveDeviceEntity> entities) {
        List<MemberConnectionInfo> connectionInfos = entities.stream()
                .map(ActiveDeviceEntity::getDeviceId)
                .map(memberRepository::findByDeviceId)
                .map(opt -> opt.orElse(null)).filter(Objects::nonNull)
                .map(Member::getId)
                .map(connectionInfoRepository::findByMemberId)
                .map(opt -> opt.orElse(null)).filter(Objects::nonNull)
                .peek(MemberConnectionInfo::activeOn)
                .toList();

        connectionInfoRepository.saveAll(connectionInfos);
    }

    private Member findOwner(ActiveDeviceEntity activeDevice){
        UUID deviceId = activeDevice.getDeviceId();

        return memberRepository.findByDeviceId(deviceId).orElse(null);
    }

}

