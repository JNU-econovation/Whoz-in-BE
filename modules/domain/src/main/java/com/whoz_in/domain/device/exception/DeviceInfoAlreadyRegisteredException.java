package com.whoz_in.domain.device.exception;

import com.whoz_in.domain.shared.BusinessException;

public class DeviceInfoAlreadyRegisteredException extends BusinessException {
    private DeviceInfoAlreadyRegisteredException(String errorMessage) {
        super("3050", errorMessage);
    }
    public static DeviceInfoAlreadyRegisteredException of(String ssid){
        return new DeviceInfoAlreadyRegisteredException(String.format("%s 대한 맥 주소가 존재합니다.", ssid));
    }
}
