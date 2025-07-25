package com.whoz_in.domain.device.exception;

import com.whoz_in.shared.WhozinException;

public class NoDeviceInfoException extends WhozinException {
    private NoDeviceInfoException(String errorMessage) {
        super("3040", errorMessage);
    }

    public static NoDeviceInfoException of(String ssid){
        return new NoDeviceInfoException(String.format("%s에 대해 등록된 맥 주소가 없습니다.", ssid));
    }
}
