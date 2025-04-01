package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.model.IpAddress;
import com.whoz_in.main_api.command.shared.application.Command;
import com.whoz_in.main_api.shared.jwt.tokens.DeviceRegisterToken;
import jakarta.annotation.Nullable;

public record DeviceInfoTempAdd(
//        String room, // 전산원 말고 다른 동방도 고려하여 구현하게 될 경우 추가할 것
        DeviceRegisterToken token,
        IpAddress ip, // TODO: 현재 위조한 요청을 막지 않음
        @Nullable String ssidHint
) implements Command {
    public DeviceInfoTempAdd(DeviceRegisterToken token, String ip, String ssid) {
        this(token, IpAddress.create(ip), ssid);
    }
}
