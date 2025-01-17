package com.whoz_in.domain.device.exception;

import com.whoz_in.domain.shared.BusinessException;

public class InvalidMacAddressException extends BusinessException {
    public static final InvalidMacAddressException EXCEPTION = new InvalidMacAddressException();
    private InvalidMacAddressException() {
        super("3005", "유효하지 않은 Mac 주소입니다.");
    }
}
