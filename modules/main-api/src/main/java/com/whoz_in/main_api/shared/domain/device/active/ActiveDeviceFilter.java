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
            log.info("[ActiveDeviceFilter] 처리할 정보 없음");
            return List.of();
        }

        List<Device> devices = uniqueLogs.stream()
                .map(log -> deviceByMac.get(log.getMac()))
                .filter(Objects::nonNull) // MonitorLog 에 있는 mac 주소 중, WhozIn에 등록되지 않은 mac 제거
                .toList();

        if(devices.isEmpty()){
            log.info("[ActiveDeviceFilter] 처리할 정보 없음");
            return List.of();
        }

        return devices;
    }


    @Override
    protected boolean judge(Device device) {
        // 이미 MonitorLog 에 존재하는 기기이다.

        UUID deviceId = device.getId().id();
        ActiveDevice activeDevice;
        try {
            activeDevice = activeDeviceViewer.getByDeviceId(deviceId.toString());
        } catch (IllegalArgumentException e){
            log.error("[ActiveDeviceFilter] 처음 Active 상태가 된 기기 {}", deviceId);
            return true;
        }


        if(!activeDevice.isActive()) {
            log.info("[ActiveDeviceFilter] Active 전환 : {}", deviceId);
            return true; // 현재 inActive 상태인데, MonitorLog 에 존재할 경우
        }

        // 이미 active 인 경우 이벤트에서 제외
        return false;
    }

    @Override
    protected void raiseEvent(List<Device> devices) {
        List<UUID> deviceIds = devices.stream().map(Device::getId).map(DeviceId::id).toList();
        Events.raise(new ActiveDeviceFinded(deviceIds));
    }
}
