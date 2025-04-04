package com.whoz_in.domain.device.exception;

import com.whoz_in.shared.WhozinException;

public class DeviceAlreadyRegisteredException extends WhozinException {
    public static final DeviceAlreadyRegisteredException EXCEPTION = new DeviceAlreadyRegisteredException();
    private DeviceAlreadyRegisteredException() {
        super("3030", "이미 등록된 기기입니다.");
    }
}
