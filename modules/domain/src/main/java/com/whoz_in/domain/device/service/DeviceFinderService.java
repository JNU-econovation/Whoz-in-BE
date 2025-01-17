package com.whoz_in.domain.device.service;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.exception.DeviceAlreadyRegisteredException;
import com.whoz_in.domain.device.exception.NoDeviceException;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.shared.DomainService;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DeviceFinderService {
    private final DeviceRepository deviceRepository;

    public Device find(DeviceId deviceId){
        return deviceRepository.findByDeviceId(deviceId).orElseThrow(()-> NoDeviceException.EXCEPTION);
    }

    public void mustNotExist(DeviceId deviceId){
        if (deviceRepository.findByDeviceId(deviceId).isPresent())
            throw DeviceAlreadyRegisteredException.EXCEPTION;
    }

    public void mustNotExistByMac(String mac){
        if (deviceRepository.findByMac(mac).isPresent())
            throw DeviceAlreadyRegisteredException.EXCEPTION;
    }

}
