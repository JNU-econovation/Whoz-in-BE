package com.whoz_in.main_api.command.device_connection;

import com.whoz_in.domain.device_connection.DeviceConnection;
import com.whoz_in.domain.device_connection.DeviceConnectionRepository;
import com.whoz_in.domain.shared.event.EventBus;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceDisconnector {
    private final EventBus eventBus;
    private final DeviceConnectionRepository deviceConnectionRepository;

    // TODO: 배치 처리
    @Transactional
    public void disconnect(DeviceConnection connection, LocalDateTime at) {
        connection.disconnect(at);
        deviceConnectionRepository.save(connection);
        eventBus.publish(connection.pullDomainEvents());
        log.info("[Disconnected Device] {}가 {}에서 연결 해제됨", connection.getDeviceId(), connection.getRoom());
    }
}
