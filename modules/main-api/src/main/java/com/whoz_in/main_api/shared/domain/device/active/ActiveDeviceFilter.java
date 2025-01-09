package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.query.device.application.active.ActiveDevice;
import com.whoz_in.main_api.query.device.application.active.ActiveDeviceViewer;
import com.whoz_in.main_api.query.device.application.active.event.ActiveDeviceFinded;
import com.whoz_in.main_api.shared.event.Events;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ActiveDeviceFilter extends DeviceFilter {

    public ActiveDeviceFilter(
            DeviceRepository deviceRepository,
            MonitorLogRepository monitorLogRepository,
            ActiveDeviceViewer activeDeviceViewer) {
        super(deviceRepository, monitorLogRepository, activeDeviceViewer);
    }

    @Override
    protected List<Device> find() {
        Set<MonitorLog> uniqueLogs = getUniqueMonitorLogs();

        if(deviceByMac.keySet().isEmpty() || deviceById.keySet().isEmpty() || uniqueLogs.isEmpty()){
            log.info("[ActiveDeviceFind] 처리할 정보 없음");
            return List.of();
        }

        return uniqueLogs.stream()
                .map(log -> deviceByMac.get(log.getMac()))
                .filter(Objects::nonNull)
                .toList();
    }


    // 1. isActive = false 인 경우 -> true
    // 2. isActive = true 인 경우 -> true
    // 3. 어떤 상황에 false 일까?
    @Override
    protected boolean judge(Device device) {
        // 이미 MonitorLog 에 존재하는 기기이다.

        UUID deviceId = device.getId().id();
        ActiveDevice activeDevice;
        try {
            activeDevice = activeDeviceViewer.getByDeviceId(deviceId.toString());
        } catch (IllegalArgumentException e){
            log.error("[Active - judge] 예상치 못한 에러 발생");
            throw new IllegalStateException(e.getMessage());
        }

        if(!activeDevice.isActive()) return true; // 현재 inActive 상태인데, MonitorLog 에 존재할 경우

        return true;
    }

    @Override
    protected void raiseEvent(List<Device> devices) {
        List<UUID> deviceIds = devices.stream().map(Device::getId).map(DeviceId::id).toList();
        Events.raise(new ActiveDeviceFinded(deviceIds));
    }

    private Set<MonitorLog> getUniqueMonitorLogs(){
        LocalDateTime before10Minute = LocalDateTime.now().minusMinutes(10);
        List<MonitorLog> logs = monitorLogRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(before10Minute); // 10분 전 로그 조회
        return new HashSet<>(logs); // 중복 제거
    }
}
