package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.main_api.command.shared.application.Command;
import java.util.UUID;

public record DeviceInfoEdit(
    UUID deviceId,
    //room이 2개 이상일 경우도 고려하게 되면 room도 받아야 함
    String ssid,
    String macToRewrite
) implements Command{
    public DeviceId getDeviceId(){
        return new DeviceId(deviceId);
    }
}
