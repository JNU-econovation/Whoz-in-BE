package com.whoz_in.api_query_jpa.device.event;

import com.whoz_in.api_query_jpa.device.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.device.InMemoryActiveDeviceRepository;
import com.whoz_in.main_api.query.device.application.active.event.ActiveDeviceFinded;
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

        List<UUID> deviceIdFromDB = activeDeviceEntities.stream().map(ActiveDeviceEntity::getDeviceId).toList();

        List<ActiveDeviceEntity> firstActiveDevices = deviceIds.stream()
                .filter(deviceId -> deviceIdFromDB.stream().noneMatch(idFromDB -> idFromDB.equals(deviceId)))
                .map(ActiveDeviceEntity::create)
                .peek(activeDevice -> activeDevice.activeOn(LocalDateTime.now()))// TODO: active Time 을 이 시점으로 설정해도 될까?
                .toList();


        List<ActiveDeviceEntity> nonFirstActiveDevice = deviceIds.stream()
                .filter(deviceId -> deviceIdFromDB.stream().anyMatch(idFromDB -> idFromDB.equals(deviceId)))
                .map(deviceId -> {
                        return activeDeviceEntities.stream()
                                    .filter(active -> active.getDeviceId().equals(deviceId))
                                    .findAny()
                                    .orElse(null);
                        })
                .filter(Objects::nonNull)
                .toList();


        saveFirstActive(firstActiveDevices);
        saveNonFirstActive(nonFirstActiveDevice);
    }

    private void saveFirstActive(List<ActiveDeviceEntity> entities){
        // 처음으로 Active 된 Device 는 그냥 저장 가능
        activeDeviceRepository.saveAll(entities);
    }

    private void saveNonFirstActive(List<ActiveDeviceEntity> entities){
        entities.forEach(activeDevice -> {
                    if(!activeDevice.isActive()) activeDevice.activeOn(LocalDateTime.now());
                });

        activeDeviceRepository.saveAll(entities); // TODO: 변경 감지 적용
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    @EventListener(ActiveDeviceFinded.class)
    public void createEmptyActiveDevice(ActiveDeviceFinded event) {

        List<UUID> deviceIds = event.getDevices();
        List<ActiveDeviceEntity> activeDeviceEntities = activeDeviceRepository.findAll();

    }
}

