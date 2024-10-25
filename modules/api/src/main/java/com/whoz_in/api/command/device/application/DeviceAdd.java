package com.whoz_in.api.command.device.application;

import com.whoz_in.api.shared.application.command.Command;
import com.whoz_in.domain.device.model.IpAddress;
import com.whoz_in.domain.device.model.MacAddress;
import lombok.Getter;

@Getter
public final class DeviceAdd implements Command {
    private final MacAddress macAddress;
    private final IpAddress ipAddress;

    public DeviceAdd(String macAddress, String ipAddress) {
        this.macAddress = MacAddress.create(macAddress);
        this.ipAddress = IpAddress.create(ipAddress);
    }
}
