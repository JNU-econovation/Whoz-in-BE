package com.whoz_in.api_query_jpa.device.active.event;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.shared.service.DeviceConnectionService;
import com.whoz_in.main_api.shared.domain.device.active.event.DeviceDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component("ActiveDeviceDeletedEventHandler")
@RequiredArgsConstructor
public class ActiveDeviceDeletedEventHandler {

    private final ActiveDeviceRepository activeDeviceRepository;
    private final DeviceConnectionService deviceConnectionService;

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    @EventListener(DeviceDeletedEvent.class)
    public void handle(DeviceDeletedEvent event) {
        // Device를 inActive 처리, DailyTime 까지 자동으로 업데이트
        deviceConnectionService.disconnectDevice(event.deviceId());
//        activeTimeUpdateWriter.updateDailyTime(event.deviceId());
        activeDeviceRepository.deleteById(event.deviceId());
    }

}
