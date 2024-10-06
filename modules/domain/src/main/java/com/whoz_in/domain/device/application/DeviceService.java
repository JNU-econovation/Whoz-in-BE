package com.whoz_in.domain.device.application;

import com.whoz_in.domain.device.domain.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public void save(){
        deviceRepository.save();
    }
}
