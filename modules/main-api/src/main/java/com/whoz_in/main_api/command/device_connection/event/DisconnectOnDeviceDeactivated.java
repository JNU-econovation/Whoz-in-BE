package com.whoz_in.main_api.command.device_connection.event;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.device_connection.DeviceConnectionRepository;
import com.whoz_in.main_api.command.device_connection.DeviceDisconnector;
import com.whoz_in.shared.domain_event.device.DeviceDeactivated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

// 삭제된 기기가 연결되어있으면 연결 해제
@Component
@RequiredArgsConstructor
public class DisconnectOnDeviceDeactivated {
    private final DeviceConnectionRepository deviceConnectionRepository;
    private final DeviceDisconnector deviceDisconnector;

    @TransactionalEventListener(phase = BEFORE_COMMIT) // 삭제 시 연결 해제 필수라서 같은 트랜잭션으로 처리
    public void handle(DeviceDeactivated event) {
        deviceConnectionRepository.findConnectedByDeviceId(new DeviceId(event.getDeviceId()))
                .ifPresent(conn -> {
                    deviceDisconnector.disconnect(conn, event.getOccurredOn());
                });
    }
}
