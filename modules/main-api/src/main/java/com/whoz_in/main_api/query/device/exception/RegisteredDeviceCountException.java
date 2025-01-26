package com.whoz_in.main_api.query.device.exception;

import com.whoz_in.main_api.shared.application.ApplicationException;

public class RegisteredDeviceCountException extends ApplicationException {
    public static final RegisteredDeviceCountException EXCEPTION = new RegisteredDeviceCountException();
    public RegisteredDeviceCountException(){ super("3060","1개 이상의 기기를 등록해야 합니다.");}
}