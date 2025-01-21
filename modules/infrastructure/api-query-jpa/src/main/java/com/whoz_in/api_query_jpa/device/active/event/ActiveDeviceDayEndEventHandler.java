package com.whoz_in.api_query_jpa.device.active.event;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import com.whoz_in.api_query_jpa.shared.util.ActiveTimeUpdateWriter;
import com.whoz_in.main_api.shared.domain.member.event.DayEndEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 하루가 종료되면 (자정이 되었을 때)
 * 접속 중인 Device 들의 DailyTime 을 초기화하고, ActiveTime 업데이트
 */
@Component
@RequiredArgsConstructor
public class ActiveDeviceDayEndEventHandler {

    private final ActiveDeviceRepository activeDeviceRepository;
    private final MemberConnectionInfoRepository connectionInfoRepository;
    private final MemberRepository memberRepository;
    private final ActiveTimeUpdateWriter activeTimeUpdateWriter;

    @EventListener(DayEndEvent.class)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void shutDownDevice(){
        List<ActiveDeviceEntity> activeDevices = activeDeviceRepository.findAll();

        List<ActiveDeviceEntity> actives = filterActive(activeDevices);
        List<ActiveDeviceEntity> inActives = filterInActive(activeDevices);

        // active 상태인 기기 모두 shutdown
        shutdown(actives);
        // active 상태인 기기들 shutdown 후, ActiveTime 업데이트
        updateActiveTime(actives);

        // DailyTime 을 초기화
        clearDailyTime(actives);
        clearDailyTime(inActives);
    }

    private List<ActiveDeviceEntity> filterActive(List<ActiveDeviceEntity> activeDevices){
        return activeDevices.stream()
                .filter(ActiveDeviceEntity::isActive)
                .toList();
    }

    private List<ActiveDeviceEntity> filterInActive(List<ActiveDeviceEntity> activeDevices){
        return activeDevices.stream()
                .filter(activeDevice -> !activeDevice.isActive())
                .toList();
    }

    private void shutdown(List<ActiveDeviceEntity> activeDevices){
        LocalDateTime midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        activeDevices.forEach(activeDevice -> activeDevice.disConnect(midnight));
    }

    private void updateActiveTime(List<ActiveDeviceEntity> activeDevices) {
        List<UUID> deviceIds = activeDevices.stream().map(ActiveDeviceEntity::getDeviceId).toList();

        // TODO: SQL 최적화
        deviceIds.stream()
                .peek(activeTimeUpdateWriter::updateDailyTime)
                .forEach(activeTimeUpdateWriter::updateTotalActiveTime);
    }

    private void clearDailyTime(List<ActiveDeviceEntity> actives) {
        List<UUID> deviceIds = actives.stream().map(ActiveDeviceEntity::getDeviceId).toList();
        deviceIds.forEach(activeTimeUpdateWriter::clearDailyTime);
    }

}
