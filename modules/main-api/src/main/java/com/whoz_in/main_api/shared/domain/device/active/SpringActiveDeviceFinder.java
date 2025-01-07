package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.active.ActiveDeviceFinder;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 이 클래스는 ActiveDevice 를 찾기보단, ActiveDevice 와 InActiveDevice 를 모두 찾고 판별한다.
// 그리고 찾은 기기들을 저장하는 이벤트를 발생시킨다.
// View 랑 Aggregate 를 같이 사용하는 게 맞을까
@Component("SpringActiveDeviceFinder")
public class SpringActiveDeviceFinder implements ActiveDeviceFinder {

    private final DeviceRepository deviceRepository;
    private final MonitorLogRepository monitorLogRepository;
    private final ActiveDeviceViewer activeDeviceViewer;
    private final Map<String, Device> deviceByMac;
    private final Map<UUID, Device> deviceById;

    private static final Duration MEASURE = Duration.ofMinutes(10); // 측정 시간 5분

    public SpringActiveDeviceFinder(
            DeviceRepository deviceRepository,
            MonitorLogRepository monitorLogRepository,
            ActiveDeviceViewer activeDeviceViewer
    ){
        this.deviceRepository = deviceRepository;
        this.monitorLogRepository = monitorLogRepository;
        this.activeDeviceViewer = activeDeviceViewer;
        this.deviceByMac = createDeviceMapByMac();
        this.deviceById = createDeviceMapById();
    }


    @Override
    @Scheduled(fixedRate = 5000)
    public void activeDeviceFind() {
        LocalDateTime todayMidnight = LocalDate.now().atTime(LocalTime.of(0, 0, 0));

        List<MonitorLog> logs = monitorLogRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(todayMidnight); // 오늘 자정 이후의 로그들 , 현재 시간으로 바꿔야 할까?
        Set<MonitorLog> uniqueLogs = new HashSet<>(logs); // 중복 제거

        if (!uniqueLogs.isEmpty()) {
            // 실제 판별 로직
            // 판별 로직이 허술하다. 판별하는 decider 를 구현해야 하나
            List<Device> activeDevices = uniqueLogs.stream()
                    .map(log -> deviceByMac.get(log.getMac()))
                    .filter(Objects::nonNull)
                    .toList();

            Events.raise(new ActiveDeviceFinded(activeDevices, uniqueLogs.stream().toList()));
        }
    }

    @Override
    @Scheduled(fixedRate = 5000)
    public void inActiveDeviceFind() {
        List<ActiveDevice> activeDevices = activeDeviceViewer.findAll();

        List<MonitorLog> logs = monitorLogRepository.findAll();
        List<UUID> monitorLogDeviceIds = logs.stream()
                .map(log -> deviceByMac.get(log.getMac()))
                .filter(Objects::nonNull)
                .map(device -> device.getId().id())
                .toList();
        Map<UUID, LocalDateTime> logTimeByDeviceId = logs.stream()
                .collect(Collectors.toMap(
                        log->deviceByMac.get(log.getMac()).getId().id(),
                        MonitorLog::getUpdatedAt
                        ));


        activeDevices = activeDevices.stream()
                .filter(activeDevice -> !monitorLogDeviceIds.contains(activeDevice.deviceId()))
                .filter(activeDevice -> {
                    LocalDateTime logCreatedTime = logTimeByDeviceId.get(activeDevice.deviceId());
                    LocalDateTime activeDeviceActiveTime = activeDevice.connectedTime();

                    Duration term = Duration.between(logCreatedTime, activeDeviceActiveTime);

                    boolean isInActive = term.compareTo(MEASURE) > 0; // 로그 발생 시간과의 차이가 기준치보다 클 경우 InActive

                    return isInActive;
                })// 모니터 로그에 없는 Device 만 추출 (InActive 후보)
                .toList();

        List<Device> inActiveDevices = activeDevices.stream()
                .map(activeDevice -> {
                    UUID id = activeDevice.deviceId();
                    return deviceById.get(id);
                })
                .toList();

        Events.raise(new InActiveDeviceFinded(inActiveDevices));
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
