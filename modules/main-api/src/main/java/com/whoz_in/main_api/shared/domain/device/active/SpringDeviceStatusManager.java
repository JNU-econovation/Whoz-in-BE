package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDeviceViewer;
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

}
