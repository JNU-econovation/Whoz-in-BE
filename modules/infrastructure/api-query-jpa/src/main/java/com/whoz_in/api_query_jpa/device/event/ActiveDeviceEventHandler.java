package com.whoz_in.api_query_jpa.device.event;

import com.whoz_in.api_query_jpa.device.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.ActiveDeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.main_api.shared.domain.device.active.event.ActiveDeviceFinded;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveDeviceEventHandler {

    private final ActiveDeviceRepository activeDeviceRepository;

    @EventListener(ActiveDeviceFinded.class)
    public void handle(ActiveDeviceFinded event) {
        List<Device> devices = event.getDevices();
        List<ActiveDeviceEntity> entities = devices.stream()
                        .map(device -> ActiveDeviceEntity.create(device.getId(), LocalDateTime.now())) // TODO: active Time 을 이 시점으로 설정해도 될까?
                        .toList();

        activeDeviceRepository.saveAll(entities);
    }

}
