package com.whoz_in.main_api.command.device_connection;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.device.model.DeviceInfo;
import com.whoz_in.domain.device.model.MacAddress;
import com.whoz_in.domain.device_connection.DeviceConnection;
import com.whoz_in.domain.device_connection.DeviceConnectionRepository;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.domain.shared.event.EventBus;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceDisconnectedChecker {
    private final DeviceRepository deviceRepository;
    private final DeviceConnectionRepository deviceConnectionRepository;
    private final MonitorLogRepository monitorLogRepository;
    private final EventBus eventBus;

    @Async
    @Transactional
    public void updateDisconnected(Set<String> activeMacs) {
        // DeviceId에 연결된 DeviceConnection 매핑
        Map<DeviceId, DeviceConnection> deviceIdToConnection = deviceConnectionRepository.findAllConnected().stream()
                .collect(Collectors.toMap(DeviceConnection::getDeviceId, conn -> conn));
        List<Device> connectedDevices = deviceRepository.findByDeviceIds(
                deviceIdToConnection.keySet()
        );

        connectedDevices.stream()
                // 활동 없는 기기만 고르기
                .filter(device -> device.getDeviceInfos().stream()
                        .noneMatch(info -> activeMacs.contains(info.getMac().toString())))
                // 연결 해제
                .forEach(device -> { // 같은 업데이트 주기에 끊기는 기기는 많지 않을거라 배치 처리 안함
                    DeviceConnection connection = deviceIdToConnection.get(device.getId());
                    MonitorLog latestLog = findDeviceLatestLogByRoom(device, connection.getRoom());
                    connection.disconnect(latestLog.getUpdatedAt());
                    deviceConnectionRepository.save(connection);
                    eventBus.publish(connection.pullDomainEvents());
                });
    }

    private MonitorLog findDeviceLatestLogByRoom(Device device, String room) {
        // 해당 기기가 가진 mac들
        Set<String> macs = device.getDeviceInfos().stream()
                .map(DeviceInfo::getMac)
                .map(MacAddress::toString)
                .collect(Collectors.toSet());
        // 가장 최신 로그 찾기
        return monitorLogRepository.findLatestByRoomAndMacs(room, macs)
                .orElseThrow(() -> new IllegalStateException(device.getId() + "의 monitor log들이 임의적으로 삭제됨"));
    }
}
