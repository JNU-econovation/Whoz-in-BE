package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDevice;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDeviceViewer;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.shared.domain.device.active.event.ActiveDeviceFinded;
import com.whoz_in.main_api.shared.event.Events;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ActiveDeviceFilter extends DeviceFilter {

    public ActiveDeviceFilter(
            DeviceRepository deviceRepository,
            MonitorLogRepository monitorLogRepository,
            ActiveDeviceViewer activeDeviceViewer,
            MemberViewer memberViewer) {
        super(deviceRepository, monitorLogRepository, activeDeviceViewer, memberViewer);
    }

    @Override
    protected List<Device> find() {
        Set<String> macs = getUniqueMonitorLogs().stream()
                .map(MonitorLog::getMac)
                .collect(Collectors.toSet());

        if(macs.isEmpty()){
            log.info("[ActiveDeviceFilter] 처리할 정보 없음");
            return List.of();
        }

        List<Device> devices = deviceRepository.findByMacs(macs);

        if(devices.isEmpty()){
            log.info("[ActiveDeviceFilter] 처리할 정보 없음");
            return List.of();
        }

        return devices;
    }

    // 이미 MonitorLog 에 존재하고 WhozIn에 등록된 기기를 Active 상태인지 아닌지 판별
    @Override
    protected boolean judge(Device device) {
        UUID deviceId = device.getId().id();
        UUID ownerId = device.getMemberId().id();

        ActiveDevice activeDevice = activeDeviceViewer.findByDeviceId(deviceId.toString()).orElse(null);

        if(!activeDevice.isActive()) {
            log.info("[ActiveDeviceFilter] Active 전환 (deviceId) : {}", deviceId);
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
