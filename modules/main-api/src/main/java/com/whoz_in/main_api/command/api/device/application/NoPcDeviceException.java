package com.whoz_in.main_api.command.api.device.application;

import com.whoz_in.shared.WhozinException;

public class NoPcDeviceException extends WhozinException {
    public static final NoPcDeviceException EXCEPTION = new NoPcDeviceException();

    private NoPcDeviceException() {
        super("3031", "첫 기기는 pc여야 합니다.");
    }
}
