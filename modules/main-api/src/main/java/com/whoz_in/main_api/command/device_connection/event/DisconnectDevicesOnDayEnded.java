package com.whoz_in.main_api.command.device_connection.event;

import com.whoz_in.domain.device_connection.DeviceConnectionRepository;
import com.whoz_in.main_api.command.device_connection.DeviceDisconnector;
import com.whoz_in.shared.DayEnded;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// 하루가 끝났을때 연결된 기기들을 초기화(연결 해제)
@Slf4j
@Component
@RequiredArgsConstructor
public class DisconnectDevicesOnDayEnded {
    private final DeviceConnectionRepository deviceConnectionRepository;
    private final DeviceDisconnector deviceDisconnector;

    @EventListener
    public void handle(DayEnded event) {
        deviceConnectionRepository.findAllConnected()
                .forEach(deviceConnection -> {
                    deviceDisconnector.disconnect(deviceConnection, event.endedAt());
                });
    }
}
