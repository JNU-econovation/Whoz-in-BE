package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDeviceViewer;
import com.whoz_in.main_api.query.member.application.MemberConnectionInfo;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

// Device 들을 받으면, 정해진 기준에 맞춰 거르는 역할이라는 뜻
@Component
public abstract class DeviceFilter {

    protected final DeviceRepository deviceRepository;
    protected final MonitorLogRepository monitorLogRepository;
    protected final ActiveDeviceViewer activeDeviceViewer;
    protected final MemberViewer memberViewer;

    public DeviceFilter(
            DeviceRepository deviceRepository,
            MonitorLogRepository monitorLogRepository,
            ActiveDeviceViewer activeDeviceViewer,
            MemberViewer memberViewer
    ) {
        this.deviceRepository = deviceRepository;
        this.monitorLogRepository = monitorLogRepository;
        this.activeDeviceViewer = activeDeviceViewer;
        this.memberViewer = memberViewer;
    }

    public void execute(){
        List<Device> devices = find();
        devices = devices.stream()
                .filter(this::judge)
                .toList();
        raiseEvent(devices);
    }

    protected Set<MonitorLog> getUniqueMonitorLogs(){
        LocalDateTime before1Minute = LocalDateTime.now().minusMinutes(1);
        List<MonitorLog> logs = monitorLogRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(before1Minute); // 1분 전 로그 조회
        return new HashSet<>(logs); // TODO: 중복 제거
    }

    protected abstract List<Device> find();
    protected abstract boolean judge(Device device);
    protected abstract void raiseEvent(List<Device> devices);

}
