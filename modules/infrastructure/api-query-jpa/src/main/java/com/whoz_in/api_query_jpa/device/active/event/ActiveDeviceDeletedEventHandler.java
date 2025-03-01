package com.whoz_in.api_query_jpa.device.active.event;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.shared.util.ActiveTimeUpdateWriter;
import com.whoz_in.main_api.shared.domain.device.active.event.DeviceDeletedEvent;
import com.whoz_in.main_api.shared.event.fail.FailedEventStore;
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
    private final ActiveTimeUpdateWriter activeTimeUpdateWriter;

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    @EventListener(DeviceDeletedEvent.class)
    public void handle(DeviceDeletedEvent event) {
        try {
            activeTimeUpdateWriter.updateDailyTime(event.deviceId());
            activeDeviceRepository.deleteById(event.deviceId());
        } catch (Exception e) {
            FailedEventStore.add(event);
        }
    }

}
