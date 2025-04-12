package com.whoz_in.domain.device_connection;

import com.whoz_in.shared.WhozinException;

public class DeviceAlreadyDisconnectedException extends WhozinException {
    public static final DeviceAlreadyDisconnectedException EXCEPTION = new DeviceAlreadyDisconnectedException();
    private DeviceAlreadyDisconnectedException() {
        super("3070", "이미 끊긴 연결입니다.");
    }
}
