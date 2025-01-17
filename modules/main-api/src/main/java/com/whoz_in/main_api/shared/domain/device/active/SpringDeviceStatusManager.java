package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.query.device.application.active.ActiveDevice;
import com.whoz_in.main_api.query.device.application.active.ActiveDeviceViewer;
import com.whoz_in.main_api.query.device.application.active.event.ActiveDeviceFinded;
import com.whoz_in.main_api.query.device.application.active.event.InActiveDeviceFinded;
import com.whoz_in.main_api.shared.event.Events;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 이 클래스는 ActiveDevice 를 찾기보단, ActiveDevice 와 InActiveDevice 를 모두 찾고 판별한다.
// 그리고 찾은 기기들을 저장하는 이벤트를 발생시킨다.
// View 랑 Aggregate 를 같이 사용하는 게 맞을까
@Component("SpringDeviceStatusManager")
@Slf4j
public class SpringDeviceStatusManager implements DeviceStatusManager {

    private final DeviceRepository deviceRepository;
    private final MonitorLogRepository monitorLogRepository;
    private final ActiveDeviceViewer activeDeviceViewer;
    private final Map<String, Device> deviceByMac;
    private final Map<UUID, Device> deviceById;

    private final InActiveDeviceFilter inActiveDeviceFilter;
    private final ActiveDeviceFilter activeDeviceFilter;

    public SpringDeviceStatusManager(
            DeviceRepository deviceRepository,
            MonitorLogRepository monitorLogRepository,
            ActiveDeviceViewer activeDeviceViewer,
            InActiveDeviceFilter inActiveDeviceFilter,
            ActiveDeviceFilter activeDeviceFilter
    ){
        this.deviceRepository = deviceRepository;
        this.monitorLogRepository = monitorLogRepository;
        this.activeDeviceViewer = activeDeviceViewer;
        this.inActiveDeviceFilter = inActiveDeviceFilter;
        this.activeDeviceFilter = activeDeviceFilter;
        this.deviceByMac = createDeviceMapByMac();
        this.deviceById = createDeviceMapById();
    }


    @Override
    @Scheduled(fixedRate = 5000)
    public void activeDeviceFind() {
        activeDeviceFilter.execute();
    }


    @Override
    @Scheduled(fixedRate = 5000)
    public void inActiveDeviceFind() {
        inActiveDeviceFilter.execute();
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

}
