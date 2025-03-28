package com.whoz_in.api_query_jpa.device.active.event;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.shared.service.DeviceConnectionService;
import com.whoz_in.api_query_jpa.shared.service.DeviceService;
import com.whoz_in.api_query_jpa.shared.service.MemberConnectionService;
import com.whoz_in.main_api.shared.domain.member.event.DayEndEvent;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 하루가 종료되면 (자정이 되었을 때)
 * 접속 중인 Device 들의 DailyTime 을 초기화하고, ActiveTime 업데이트
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ActiveDeviceDayEndEventHandler {

    private final ActiveDeviceRepository activeDeviceRepository;
    private final DeviceConnectionService deviceConnectionService;
    private final MemberConnectionService memberConnectionService;
    private final DeviceService deviceService;

    @EventListener(DayEndEvent.class)
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void handle(){
        log.info("[DayEndEvent] 발생 : 접속 중인 기기를 shutdown 하고, DailyTime 을 초기화 합니다.");
        List<ActiveDeviceEntity> activeDevices = activeDeviceRepository.findAll();

        List<ActiveDeviceEntity> actives = filterActive(activeDevices);
        List<ActiveDeviceEntity> inActives = filterInActive(activeDevices);

        LocalDateTime time = LocalDateTime.now();

        // active 상태인 기기 모두 접속 종료 / dailyTime 업데이트
        actives.stream()
                .map(ActiveDeviceEntity::getDeviceId)
                .forEach(id -> deviceConnectionService.disconnectDevice(id,time));

        // totalTime 업데이트 및 dailyTime 초기화
        actives.stream()
                .map(ActiveDeviceEntity::getMemberId)
                .forEach(memberConnectionService::updateTotalTime);

        inActives.stream()
                .map(ActiveDeviceEntity::getMemberId)
                .forEach(memberConnectionService::updateTotalTime);

    }

    private List<ActiveDeviceEntity> filterActive(List<ActiveDeviceEntity> activeDevices){
        return activeDevices.stream()
                .filter(ActiveDeviceEntity::isActive)
                .toList();
    }

    private List<ActiveDeviceEntity> filterInActive(List<ActiveDeviceEntity> activeDevices) {
        return activeDevices.stream()
                .filter(activeDevice -> !activeDevice.isActive())
                .toList();
    }

}
