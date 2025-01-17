package com.whoz_in.domain.device.exception;

import com.whoz_in.domain.shared.BusinessException;

public class InvalidIPv4AddressException extends BusinessException {
    public static final InvalidIPv4AddressException EXCEPTION = new InvalidIPv4AddressException();
    private InvalidIPv4AddressException() {
        super("3006", "유효하지 않은 IPv4 주소입니다.");
    }
}
