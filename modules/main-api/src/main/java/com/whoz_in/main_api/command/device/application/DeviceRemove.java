package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.main_api.command.shared.application.Command;
import java.util.UUID;

public record DeviceRemove(
    UUID deviceId
) implements Command {

    public DeviceId getDeviceId(){
        return new DeviceId(deviceId); //굳이 여러 번 호출할 필요 없겠죠
    }
}
