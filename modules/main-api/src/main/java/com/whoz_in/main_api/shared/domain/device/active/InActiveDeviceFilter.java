package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.query.device.application.active.ActiveDevice;
import com.whoz_in.main_api.query.device.application.active.ActiveDeviceViewer;
import com.whoz_in.main_api.query.device.application.active.event.InActiveDeviceFinded;
import com.whoz_in.main_api.shared.event.Events;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InActiveDeviceFilter extends DeviceFilter{

    // MonitorLog 가 마지막으로 뜬지 10분이 되도록 발생하지 않을경우 InActive 처리하는 기준
    private static final Duration MEASURE = Duration.ofMinutes(10);

    public InActiveDeviceFilter(
            DeviceRepository deviceRepository,
            MonitorLogRepository monitorLogRepository,
            ActiveDeviceViewer activeDeviceViewer) {
        super(deviceRepository, monitorLogRepository, activeDeviceViewer);
    }

    @Override
    protected List<Device> find() {

        List<ActiveDevice> activeDevices = activeDeviceViewer.findAll();

        if(!activeDevices.isEmpty()) {
            Set<MonitorLog> logs = getUniqueMonitorLogs();

            List<UUID> monitorLogDeviceIds = logs.stream()
                    .map(log -> deviceByMac.get(log.getMac()))
                    .filter(Objects::nonNull)
                    .map(Device::getId)
                    .map(DeviceId::id)
                    .toList();

            List<UUID> deviceIds = activeDevices.stream().map(ActiveDevice::deviceId).toList();

            return deviceIds.stream()
                    .filter(deviceId -> !monitorLogDeviceIds.contains(deviceId))
                    .map(deviceById::get)
                    .filter(Objects::nonNull)
                    .toList();

        }
        log.info("[InActiveDeviceFind] 처리할 정보 없음");
        return List.of();
    }

    // inactive 판별 다시 설계하기
    // 고려해야할 것
    // 1. monitorLog 조회 크기 (10분 등)
    // 2. 모니터 로그에 없는 맥을 제외할 것이냐 아니냐
    @Override
    protected boolean judge(Device device) {
        UUID deviceId = device.getId().id();

        ActiveDevice activeDevice = activeDeviceViewer.getByDeviceId(deviceId.toString());

        Map<UUID, LocalDateTime> logTimeByDeviceId = createLogTimeByDeviceIdMap();

        LocalDateTime logCreatedTime = logTimeByDeviceId.get(deviceId);
        LocalDateTime activeDeviceActiveTime = activeDevice.connectedTime();

        Duration term = Duration.between(activeDeviceActiveTime, logCreatedTime).abs();

        // 로그 발생 시간과의 차이가 기준치보다 클 경우 InActive
        return term.compareTo(MEASURE) > 0;
    }

    @Override
    protected void raiseEvent(List<Device> devices) {
        // 이벤트 발생
        List<UUID> deviceIds = devices.stream()
                .map(Device::getId)
                .map(DeviceId::id)
                .toList();

        Events.raise(new InActiveDeviceFinded(deviceIds));
    }

    private Map<UUID, LocalDateTime> createLogTimeByDeviceIdMap(){
        Set<MonitorLog> logs = getUniqueMonitorLogs();

        return logs.stream() // 이 기기가 언제 MonitorLog를 발생시켰는지 알기위한 맵
                .filter(log -> deviceByMac.containsKey(log.getMac()))
                .collect(Collectors.toMap(
                        log -> deviceByMac.get(log.getMac()).getId().id(),
                        MonitorLog::getUpdatedAt
                ));
    }
}
