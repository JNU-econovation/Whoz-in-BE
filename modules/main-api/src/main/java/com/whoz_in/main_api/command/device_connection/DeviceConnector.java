package com.whoz_in.main_api.command.device_connection;

import com.whoz_in.domain.device_connection.DeviceConnection;
import com.whoz_in.domain.device_connection.DeviceConnectionRepository;
import com.whoz_in.domain.shared.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeviceConnector {
    private final EventBus eventBus;
    private final DeviceConnectionRepository deviceConnectionRepository;

    @Transactional
    public void connect(DeviceConnection deviceConnection){
        deviceConnectionRepository.save(deviceConnection);
        eventBus.publish(deviceConnection.pullDomainEvents());
    }
}
