package com.whoz_in.main_api.command.device.application;

import com.whoz_in.main_api.command.shared.application.Command;

public record DeviceRegister(
        String room
) implements Command {}
