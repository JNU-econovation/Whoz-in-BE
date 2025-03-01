package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDevice;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDeviceViewer;
import com.whoz_in.main_api.query.member.application.exception.NotFoundConnectionInfoException;
import com.whoz_in.main_api.query.member.application.view.MemberConnectionInfo;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.shared.domain.device.active.event.InActiveDeviceFinded;
import com.whoz_in.main_api.shared.event.Events;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
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
            ActiveDeviceViewer activeDeviceViewer,
            MemberViewer memberViewer) {
        super(deviceRepository, monitorLogRepository, activeDeviceViewer, memberViewer);
    }

    @Override
    protected List<Device> find() {

        List<ActiveDevice> activeDevices = activeDeviceViewer.findAll();
        Set<String> macs = getUniqueMonitorLogs().stream()
                .map(MonitorLog::getMac)
                .collect(Collectors.toSet());

        if(!activeDevices.isEmpty()) {
            // DeviceFilter 는 무조건, 후즈인에 등록된 Device 만을 넘겨야 한다.
            List<UUID> monitorLogDeviceIds = getDeviceInMonitorLog(macs);

            List<UUID> notInMonitorLog = getDeviceNotInMonitorLog(monitorLogDeviceIds, activeDevices);

            return findDevices(notInMonitorLog);
        }

        log.info("[InActiveDeviceFilter] 처리할 정보 없음");
        return List.of();
    }

    @Override
    protected boolean judge(Device device) {
        UUID deviceId = device.getId().id();
        UUID ownerId = device.getMemberId().id();

        ActiveDevice activeDevice = activeDeviceViewer.getByDeviceId(deviceId.toString());
        MemberConnectionInfo connectionInfo = memberViewer.findConnectionInfo(ownerId.toString()).orElse(null);

        if(connectionInfo == null) throw new NotFoundConnectionInfoException();

        // 다른 기기로 접속 중인 경우 이벤트에서 제외
        if(connectionInfo.isActive())
            return false;

        // 이미 inActive 상태인 기기의 경우 이벤트에서 제외
        if(!activeDevice.isActive())
            return false;

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

    private List<UUID> getDeviceInMonitorLog(Set<String> macs) {
        return deviceRepository.findByMacs(macs).stream()
                .map(Device::getId)
                .map(DeviceId::id)
                .toList();
    }

    private List<UUID> getDeviceNotInMonitorLog(List<UUID> monitorLogDeviceIds, List<ActiveDevice> activeDevices) {
        return activeDevices.stream().map(ActiveDevice::deviceId)
                .filter(deviceId -> !monitorLogDeviceIds.contains(deviceId))
                .toList();

    }

    private List<Device> findDevices(List<UUID> deviceIds){
        return deviceRepository.findByDeviceIds(deviceIds.stream().map(DeviceId::new).toList());
    }
}
