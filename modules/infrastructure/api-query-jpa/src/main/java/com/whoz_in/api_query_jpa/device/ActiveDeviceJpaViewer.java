package com.whoz_in.api_query_jpa.device;

import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDevice;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDeviceViewer;
import java.util.List;
import java.util.Objects;
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

        return Optional.of(createOptionalActiveDevice(activeDevice));
    }

    @Override
    public List<ActiveDevice> findAll() {
        List<ActiveDeviceEntity> entities = activeDeviceRepository.findAll();

        return entities.stream()
                .map(this::createOptionalActiveDevice)
                .toList();
    }

    private ActiveDevice createOptionalActiveDevice(ActiveDeviceEntity entity){
        return new ActiveDevice(
                entity.getDeviceId(),
                entity.getConnectedTime(),
                entity.getDisConnectedTime());
    }
}
