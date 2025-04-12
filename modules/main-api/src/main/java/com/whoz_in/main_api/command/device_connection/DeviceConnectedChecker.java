package com.whoz_in.main_api.command.device_connection;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device_connection.DeviceConnection;
import com.whoz_in.domain.device_connection.DeviceConnectionRepository;
import com.whoz_in.domain.network_log.MonitorLog;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceConnectedChecker {
    private final DeviceRepository deviceRepository;
    private final DeviceConnectionRepository deviceConnectionRepository;
    private final DeviceConnector connector;

    // TODO: disconnectedAt이 일정 범위 내에 있을경우 이어서 연결된 것으로 판단할 수 있음
        // 이때 연결된 방이 다르면 x
        // 새 하루가 시작돼서 끊긴 것과는 다르게 처리해야 됨
    @Async
    public void updateConnected(List<MonitorLog> recentLogs){
        // 기기를 로그의 맥으로 매핑
        Map<String, Device> deviceByMac = mapMacToDevice(recentLogs);

        // 기기들 중에서 방이 다르거나 연결되지 않은 기기들 연결
        recentLogs.stream()
                .filter(log -> deviceByMac.containsKey(log.getMac()))
                .map(log -> Map.entry(log, deviceByMac.get(log.getMac())))
                .filter(entry -> shouldConnect(entry.getKey(), entry.getValue()))
                .forEach(entry -> connectDevice(entry.getKey(), entry.getValue()));
    }

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

    private boolean shouldConnect(MonitorLog log, Device device) {
        return deviceConnectionRepository.findLatestByDeviceId(device.getId())
                .map(conn -> !conn.isConnectedIn(log.getRoom()))
                .orElse(true);
    }

    private void connectDevice(MonitorLog monitorLog, Device device) {
        Optional<DeviceConnection> existing = deviceConnectionRepository.findLatestByDeviceId(device.getId());
        DeviceConnection newOrUpdated = existing
                .map(conn -> {
                    conn.disconnect(monitorLog.getUpdatedAt());
                    return conn;
                })
                .orElse(DeviceConnection.create(device.getId(), monitorLog.getRoom(), monitorLog.getUpdatedAt()));
        connector.connect(newOrUpdated);
        log.info("[Connected Device] {}가 {}에서 연결됨", device.getId(), newOrUpdated.getRoom());
    }
}
