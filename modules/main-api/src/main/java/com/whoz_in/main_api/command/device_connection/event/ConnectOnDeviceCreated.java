package com.whoz_in.main_api.command.device_connection.event;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.device_connection.DeviceConnection;
import com.whoz_in.domain.device_connection.DeviceConnectionRepository;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.domain.shared.event.EventBus;
import com.whoz_in.shared.domain_event.device.DeviceCreated;
import com.whoz_in.shared.domain_event.device.DeviceInfoPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

// 새로 등록된 기기가 생겼을땐 연결된 기기이므로 바로 연결 처리
@Component
@RequiredArgsConstructor
public class ConnectOnDeviceCreated {
    private final MonitorLogRepository monitorLogRepository;
    private final DeviceConnectionRepository deviceConnectionRepository;
    private final EventBus eventBus;

    @TransactionalEventListener(phase = AFTER_COMMIT) // 등록 시 연결이 필수는 아님
    @Transactional(propagation = REQUIRES_NEW)
    public void handle(DeviceCreated event) {
        monitorLogRepository.findLatestByMacs(event.getDeviceInfos().stream()
                        .map(DeviceInfoPayload::mac).toList())
                .ifPresent(monitorLog -> {
                    DeviceConnection connection = DeviceConnection.create(
                            new DeviceId(event.getDeviceId()),
                            monitorLog.getRoom(),
                            event.getOccurredOn()
                    );
                    deviceConnectionRepository.save(connection);
                    eventBus.publish(connection.pullDomainEvents());
                });
    }
}
