package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.query.device.application.active.ActiveDeviceViewer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

// Device 들을 받으면, 정해진 기준에 맞춰 거르는 역할이라는 뜻
@Component
public abstract class DeviceFilter {

    protected final DeviceRepository deviceRepository;
    protected final MonitorLogRepository monitorLogRepository;
    protected final ActiveDeviceViewer activeDeviceViewer;

    protected final Map<String, Device> deviceByMac;
    protected final Map<UUID, Device> deviceById;

    public DeviceFilter(
            DeviceRepository deviceRepository,
            MonitorLogRepository monitorLogRepository,
            ActiveDeviceViewer activeDeviceViewer
    ) {
        this.deviceRepository = deviceRepository;
        this.monitorLogRepository = monitorLogRepository;
        this.activeDeviceViewer = activeDeviceViewer;
        this.deviceByMac = createDeviceMapByMac();
        this.deviceById = createDeviceMapById();
    }

    public void execute(){
        List<Device> devices = find();
        devices = devices.stream()
                .filter(this::judge)
                .toList();
        raiseEvent(devices);
    }

    protected Set<MonitorLog> getUniqueMonitorLogs(){
        LocalDateTime before10Minute = LocalDateTime.now().minusMinutes(10);
        List<MonitorLog> logs = monitorLogRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(before10Minute); // 10분 전 로그 조회
        return new HashSet<>(logs); // 중복 제거
    }

    private Map<UUID, Device> createDeviceMapById() {
        return deviceRepository.findAll().stream()
                .collect(Collectors.toMap(device -> device.getId().id(), device -> device));
    }

    private Map<String, Device> createDeviceMapByMac(){
        return deviceRepository.findAll().stream()
                .map(this::oneDeviceToMap)
                .reduce(new HashMap<>(), (identity, deviceMap) -> {
                    identity.putAll(deviceMap);
                    return identity;
                });
    }

    // Mac 주소를 Key 로 사용한 Device Map
    private Map<String, Device> oneDeviceToMap(Device device) {
        return device.getDeviceInfos().stream()
                .collect(Collectors.toMap(deviceInfo -> deviceInfo.getMac().toString(), deviceInfo -> device));
    }

    protected abstract List<Device> find();
    protected abstract boolean judge(Device device);
    protected abstract void raiseEvent(List<Device> devices);

}
