package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.device.model.DeviceInfo;
import com.whoz_in.domain.device.model.MacAddress;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDevice;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDeviceViewer;
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
        Set<String> macs = getUniqueMonitorLogs().stream()
                .map(MonitorLog::getMac)
                .collect(Collectors.toSet());

        if(!activeDevices.isEmpty()) {
            List<UUID> monitorLogDeviceIds = deviceRepository.findByMacs(macs).stream()
                    .map(Device::getId)
                    .map(DeviceId::id)
                    .toList();

            List<DeviceId> notInMonitorLog = activeDevices.stream().map(ActiveDevice::deviceId)
                    .filter(deviceId -> !monitorLogDeviceIds.contains(deviceId))
                    .map(DeviceId::new)
                    .toList();

            return deviceRepository.findByDeviceIds(notInMonitorLog);
        }
        log.info("[InActiveDeviceFilter] 처리할 정보 없음");
        return List.of();
    }

    @Override
    protected boolean judge(Device device) {
        UUID deviceId = device.getId().id();

        ActiveDevice activeDevice = activeDeviceViewer.getByDeviceId(deviceId.toString());

        // 이미 inActive 상태인 기기의 경우 이벤트에서 제외
        if(!activeDevice.isActive()) return false;

        Map<UUID, LocalDateTime> logTimeByDeviceId = createLogTimeByDeviceIdMap();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime activeDeviceActiveTime = activeDevice.connectedTime();

        Duration term = Duration.between(activeDeviceActiveTime, now).abs();

        // 로그 발생 시간과의 차이가 기준치보다 클 경우 InActive

        if(term.compareTo(MEASURE) > 0){
            log.info("[InActiveDeviceFilter] InActive 전환 {}", deviceId);
            return true;
        }
        return false;
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
        List<Device> devices = deviceRepository.findAll();

        List<Map<MacAddress, DeviceId>> deviceInfoMap = devices.stream()
                .map(device -> {
                    return device.getDeviceInfos().stream()
                            .collect(Collectors.toMap(
                                    DeviceInfo::getMac,
                                    deviceInfo -> device.getId()
                            ));
                })
                .toList();

        return logs.stream() // 이 기기가 언제 MonitorLog를 발생시켰는지 알기위한 맵
                .filter(log -> deviceInfoMap.stream()
                        .flatMap(map -> map.keySet().stream())
                        .map(MacAddress::toString)
                        .anyMatch(mac -> mac.equals(log.getMac())) // 모니터 로그에 찍힌 mac 중에 WhozIn에 등록된 기기
                )
                .collect(Collectors.toMap(
                        log -> deviceRepository.findByMac(log.getMac()).get().getId().id(), // TODO: Optional null 처리
                        MonitorLog::getUpdatedAt
                ));
    }
}
