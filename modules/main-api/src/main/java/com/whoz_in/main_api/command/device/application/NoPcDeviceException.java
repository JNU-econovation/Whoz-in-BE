package com.whoz_in.main_api.command.device.application;

import com.whoz_in.main_api.shared.application.ApplicationException;

public class NoPcDeviceException extends ApplicationException {
    public static final NoPcDeviceException EXCEPTION = new NoPcDeviceException();

    public NoPcDeviceException() {
        super("3031", "첫 기기는 pc여야 합니다.");
    }
}
