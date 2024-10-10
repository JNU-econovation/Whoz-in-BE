package com.whoz_in.domain.device.domain.exception;

import com.whoz_in.domain.shared.domain.BusinessException;

public class InvalidMacAddressException extends BusinessException {
    public InvalidMacAddressException() {
        super("3005", "유효하지 않은 Mac 주소입니다.");
    }
}
