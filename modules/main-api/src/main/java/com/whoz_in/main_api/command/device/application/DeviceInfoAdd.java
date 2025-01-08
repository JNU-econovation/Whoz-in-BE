package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.model.IpAddress;
import com.whoz_in.main_api.command.shared.application.Command;

public record DeviceInfoAdd(
        String room, // 다른 방에서 같은 ip가 있을 수 있기 때문에 방 이름으로 구별한다.
        IpAddress ip
) implements Command {
    public DeviceInfoAdd(String room, String ip) {
        this(room, IpAddress.create(ip));
    }
}
