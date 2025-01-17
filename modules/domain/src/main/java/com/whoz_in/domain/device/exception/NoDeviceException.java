package com.whoz_in.domain.device.exception;

import com.whoz_in.domain.shared.BusinessException;

public class NoDeviceException extends BusinessException {
    public static final NoDeviceException EXCEPTION = new NoDeviceException();
    private NoDeviceException() {
        super("3001", "등록된 기기가 없습니다.");
    }
}
