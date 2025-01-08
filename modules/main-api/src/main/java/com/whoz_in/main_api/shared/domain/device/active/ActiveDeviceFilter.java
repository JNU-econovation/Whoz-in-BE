package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.query.device.application.active.ActiveDeviceViewer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class ActiveDeviceFilter extends DeviceFilter {

    public ActiveDeviceFilter(
            DeviceRepository deviceRepository,
            MonitorLogRepository monitorLogRepository,
            ActiveDeviceViewer activeDeviceViewer) {
        super(deviceRepository, monitorLogRepository, activeDeviceViewer);
    }

    @Override
    protected List<Device> find() {
        LocalDateTime todayMidnight = LocalDate.now().atTime(LocalTime.of(0, 0, 0));

        List<MonitorLog> logs = monitorLogRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(todayMidnight); // 오늘 자정 이후의 로그들 , 현재 시간으로 바꿔야 할까?
        Set<MonitorLog> uniqueLogs = new HashSet<>(logs); // 중복 제거

        if (!uniqueLogs.isEmpty()) {
            // 실제 판별 로직
            // 판별 로직이 허술하다. 판별하는 decider 를 구현해야 하나
            return uniqueLogs.stream()
                    .map(log -> deviceByMac.get(log.getMac()))
                    .filter(Objects::nonNull)
                    .toList();

        }

        return List.of();
    }

    @Override
    protected boolean judge(Device device) {
        return false;
    }

    @Override
    protected void raiseEvent(List<Device> devices) {

    }
}
