package com.whoz_in.main_api.command.device_connection.event;

import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.device_connection.DeviceConnection;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.command.device_connection.DeviceConnector;
import com.whoz_in.shared.domain_event.device.DeviceCreated;
import com.whoz_in.shared.domain_event.device.DeviceInfoPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// 새로 등록된 기기가 생겼을땐 연결된 기기이므로 바로 연결 처리
@Component
@RequiredArgsConstructor
public class ConnectOnDeviceCreated {
    private final MonitorLogRepository monitorLogRepository;
    private final DeviceConnector deviceConnector;

    @EventListener // 등록 시 연결이 필수는 아님
    public void handle(DeviceCreated event) {
        monitorLogRepository.findLatestByMacs(event.getDeviceInfos().stream()
                        .map(DeviceInfoPayload::mac).toList())
                .ifPresent(monitorLog -> {
                    DeviceConnection connection = DeviceConnection.create(
                            new DeviceId(event.getDeviceId()),
                            monitorLog.getRoom(),
                            event.getOccurredOn()
                    );
                    deviceConnector.connect(connection);
                });
    }
}
