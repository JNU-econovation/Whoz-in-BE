package com.whoz_in.main_api.command.device_connection.event;

import com.whoz_in.domain.device_connection.DeviceConnectionRepository;
import com.whoz_in.domain.shared.event.EventBus;
import com.whoz_in.shared.DayEnded;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// 하루가 끝났을때 연결된 기기들을 초기화(연결 해제)
@Slf4j
@Component
@RequiredArgsConstructor
public class DisconnectDevicesOnDayEnded {
    private final EventBus eventBus;
    private final DeviceConnectionRepository deviceConnectionRepository;

    @EventListener
    @Transactional
    public void handle(DayEnded event) {
        deviceConnectionRepository.findAllConnected()
                .forEach(deviceConnection -> {
                    deviceConnection.disconnect(event.endedAt());
                    deviceConnectionRepository.save(deviceConnection);
                    eventBus.publish(deviceConnection.pullDomainEvents());
                });
        log.info("[DisconnectDevicesOnDayEnded] 모든 기기의 연결이 해제됨");
    }
}
