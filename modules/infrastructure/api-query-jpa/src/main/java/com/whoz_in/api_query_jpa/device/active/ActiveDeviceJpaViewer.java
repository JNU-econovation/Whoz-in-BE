package com.whoz_in.api_query_jpa.device.active;

import com.whoz_in.main_api.query.device.application.active.view.ActiveDevice;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDeviceViewer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveDeviceJpaViewer implements ActiveDeviceViewer {

    private final ActiveDeviceRepository activeDeviceRepository;

    @Override
    public Optional<ActiveDevice> findByDeviceId(String deviceId) {
        ActiveDeviceEntity activeDevice = activeDeviceRepository.findByDeviceId(UUID.fromString(deviceId)).orElse(null);

        if(activeDevice == null) return Optional.empty();

        return Optional.of(createActiveDevice(activeDevice));
    }

    @Override
    public List<ActiveDevice> findAll() {
        List<ActiveDeviceEntity> entities = activeDeviceRepository.findAll();

        return entities.stream()
                .map(this::createActiveDevice)
                .toList();
    }

    @Override
    public List<ActiveDevice> findByDeviceIds(List<String> deviceIds) {
        return activeDeviceRepository.findByDeviceIds(deviceIds.stream().map(UUID::fromString).toList()).stream()
                .map(this::createActiveDevice)
                .toList();
    }

    private ActiveDevice createActiveDevice(ActiveDeviceEntity entity){
        return new ActiveDevice(
                entity.getDeviceId(),
                entity.getConnectedAt(),
                entity.getDisConnectedAt());
    }
}
