package com.whoz_in.main_api.command.device.application;

import com.whoz_in.main_api.command.shared.application.Command;

public record DeviceRegister(
        String room,
        String deviceName
) implements Command {

    public DeviceRegister {
        if (deviceName == null || deviceName.isBlank())
            throw new IllegalArgumentException("기기 이름이 비어있음");
    }
}
