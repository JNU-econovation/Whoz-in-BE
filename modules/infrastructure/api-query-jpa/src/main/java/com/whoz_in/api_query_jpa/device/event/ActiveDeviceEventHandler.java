package com.whoz_in.api_query_jpa.device.event;

import com.whoz_in.api_query_jpa.device.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.ActiveDeviceRepository;
import com.whoz_in.main_api.shared.domain.device.active.event.ActiveDeviceFinded;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActiveDeviceEventHandler {

    private final ActiveDeviceRepository activeDeviceRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @EventListener(ActiveDeviceFinded.class)
    public void saveActiveDevices(ActiveDeviceFinded event) {
        List<UUID> deviceIds = event.getDevices();
        List<ActiveDeviceEntity> activeDeviceEntities = activeDeviceRepository.findAll();

        List<ActiveDeviceEntity> firstActiveDevices = findFirstActiveDevices(deviceIds);
        List<ActiveDeviceEntity> nonFirstActiveDevice = findNonFirstActiveDevices(deviceIds);

        save(firstActiveDevices);
        save(nonFirstActiveDevice);
    }

    private List<ActiveDeviceEntity> findFirstActiveDevices(List<UUID> deviceIds){
        return deviceIds.stream()
                .filter(activeDeviceRepository::existsByDeviceId)
                .map(ActiveDeviceEntity::create)
                .toList();
    }

    private List<ActiveDeviceEntity> findNonFirstActiveDevices(List<UUID> deviceIds){
        return deviceIds.stream()
                .map(activeDeviceRepository::findByDeviceId)
                .map(opt -> opt.orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    private void save(List<ActiveDeviceEntity> entities){
        entities.forEach(activeDevice -> activeDevice.connect(LocalDateTime.now()));
        activeDeviceRepository.saveAll(entities);
    }

}

