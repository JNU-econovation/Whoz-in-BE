package com.whoz_in.main_api.command.device_connection;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device_connection.DeviceConnection;
import com.whoz_in.domain.device_connection.DeviceConnectionRepository;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.shared.event.EventBus;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


// 기기가 연결된 상태인지 확인하고 연결 처리함
@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceConnectedChecker {
    private final DeviceRepository deviceRepository;
    private final DeviceConnectionRepository connectionRepository;
    private final EventBus eventBus;

    // TODO: monitor log가 잘 안뜨는 기기를 위해 disconnectedAt이 일정 범위 내에 있을경우 이어서 연결된 것으로 판단할 수 있음
        // 이때 연결된 방이 다르면 x
        // 새 하루가 시작돼서 끊긴 것과는 다르게 처리해야 됨
    @Async
    @Transactional
    public void updateConnected(List<MonitorLog> recentLogs) {
        // 로그의 맥으로 기기를 빠르게 찾기 위한 맵
        Map<String, Device> deviceByMac = mapMacToDevice(recentLogs);

        recentLogs.stream()
                .filter(monitorLog -> deviceByMac.containsKey(monitorLog.getMac()))
                .forEach(monitorLog -> {
                    Device device = deviceByMac.get(monitorLog.getMac());
                    Optional<DeviceConnection> existingConn = connectionRepository.findConnectedByDeviceId(device.getId());

                    // 연결 필요 여부 판단
                    if (!shouldConnect(existingConn, monitorLog.getRoom())) return;

                    // 연결 처리
                    DeviceConnection newOrUpdated = existingConn
                            .map(conn -> {
                                conn.disconnect(monitorLog.getUpdatedAt());
                                return conn;
                            })
                            .orElse(DeviceConnection.create(device.getId(), monitorLog.getRoom(), monitorLog.getUpdatedAt()));

                    connectionRepository.save(newOrUpdated);
                    eventBus.publish(newOrUpdated.pullDomainEvents());
                    log.info("[Connected Device] {}가 {}에서 연결됨", device.getId(), newOrUpdated.getRoom());
                });
    }

    // Map<mac, Device> 반환
    private Map<String, Device> mapMacToDevice(List<MonitorLog> logs) {
        List<String> macs = logs.stream().map(MonitorLog::getMac).toList();
        return deviceRepository.findByMacs(macs).stream()
                .flatMap(device -> device.getDeviceInfos().stream()
                        .map(info -> Map.entry(info.getMac().toString(), device)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing
                ));
    }

    // 기기가 연결되어 있지 않거나 방이 다를 경우 다시 연결해야 함
    private boolean shouldConnect(Optional<DeviceConnection> connection, String roomToCheck) {
        return connection
                .map(conn -> !conn.isConnectedIn(roomToCheck))
                .orElse(true);
    }
}
