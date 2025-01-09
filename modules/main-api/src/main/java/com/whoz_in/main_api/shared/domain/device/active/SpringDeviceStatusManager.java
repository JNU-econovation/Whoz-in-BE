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

    private static final Duration MEASURE = Duration.ofMinutes(5); // 측정 시간 10분

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

        LocalDateTime before10Minute = LocalDateTime.now().minusMinutes(10);
        List<MonitorLog> logs = monitorLogRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(before10Minute); // 10분 전 로그 조회
        Set<MonitorLog> uniqueLogs = new HashSet<>(logs); // 중복 제거

        if(deviceByMac.keySet().isEmpty() || deviceById.keySet().isEmpty() || uniqueLogs.isEmpty()){
            log.info("[ActiveDeviceFind] 처리할 정보 없음");
            return;
        }
        // 실제 판별 로직
        // 판별 로직이 허술하다. 판별하는 decider 를 구현해야 하나
        List<UUID> activeDevices = uniqueLogs.stream()
                .map(log -> deviceByMac.get(log.getMac()))
                .filter(Objects::nonNull)
                .map(device -> device.getId().id())
                .toList();

        Events.raise(new ActiveDeviceFinded(activeDevices, uniqueLogs.stream().toList()));

    }

    // inactive 판별 다시 설계하기
    // 고려해야할 것
    // 1. monitorLog 조회 크기 (10분 등)
    // 2. 모니터 로그에 없는 맥을 제외할 것이냐 아니냐

    @Override
    @Scheduled(fixedRate = 5000)
    public void inActiveDeviceFind() {
        List<ActiveDevice> activeDevices = activeDeviceViewer.findAll();

        if(!activeDevices.isEmpty()) {

            LocalDateTime before10Minute = LocalDateTime.now().minusMinutes(10);

            List<MonitorLog> logs = monitorLogRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(before10Minute);
            List<UUID> monitorLogDeviceIds = logs.stream()
                    .map(log -> deviceByMac.get(log.getMac()))
                    .filter(Objects::nonNull)
                    .map(device -> device.getId().id())
                    .toList();
            Map<UUID, LocalDateTime> logTimeByDeviceId = logs.stream()
                    .filter(log -> deviceByMac.containsKey(log.getMac()))
                    .collect(Collectors.toMap(
                            log -> deviceByMac.get(log.getMac()).getId().id(),
                            MonitorLog::getUpdatedAt
                    ));

            activeDevices = activeDevices.stream()
                    .filter(activeDevice -> !monitorLogDeviceIds.contains(activeDevice.deviceId())) // 모니터 로그에 없는 Device 만 추출 (InActive 후보)
                    .filter(activeDevice -> {
                        LocalDateTime logCreatedTime = logTimeByDeviceId.get(activeDevice.deviceId());
                        LocalDateTime activeDeviceActiveTime = activeDevice.connectedTime();

                        Duration term = Duration.between(activeDeviceActiveTime, logCreatedTime).abs();

                        // 로그 발생 시간과의 차이가 기준치보다 클 경우 InActive
                        return term.compareTo(MEASURE) > 0;
                    })
                    .toList();

            List<UUID> inActiveDevices = activeDevices.stream()
                    .map(activeDevice -> {
                        UUID id = activeDevice.deviceId();
                        return deviceById.get(id);
                    })
                    .map(device -> device.getId().id())
                    .toList();

            Events.raise(new InActiveDeviceFinded(inActiveDevices));
        } else {
            log.info("[InActiveDeviceFind] 처리할 정보 없음");
        }
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
