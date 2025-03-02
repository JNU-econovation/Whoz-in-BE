package com.whoz_in.api_query_jpa.shared.util;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// ActiveDevice 를 찾으면 Active 상태로 바꾸고 이에 필요한 작업을 하는 친구
@Component
@RequiredArgsConstructor
public class ActiveDeviceManager {

    private final ActiveDeviceRepository activeDeviceRepository;

    public void saveActiveDevices(List<ActiveDeviceEntity> activeDevices) {
        save(activeDevices);
    }

    private void save(List<ActiveDeviceEntity> entities){
        entities.forEach(activeDevice -> activeDevice.connect(LocalDateTime.now()));
        activeDeviceRepository.saveAll(entities);
    }

}
