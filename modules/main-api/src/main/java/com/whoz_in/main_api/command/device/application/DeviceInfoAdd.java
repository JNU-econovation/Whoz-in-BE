package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.model.IpAddress;
import com.whoz_in.main_api.command.shared.application.Command;

public record DeviceInfoAdd(
//        String room, // 전산원 말고 다른 동방도 고려하여 구현하게 될 경우 추가할 것
        IpAddress ip
) implements Command {
    public DeviceInfoAdd(String room, String ip) {
        this(IpAddress.create(ip));
    }
}
