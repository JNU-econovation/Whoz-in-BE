package com.whoz_in.main_api.command.device.application;

import com.whoz_in.main_api.command.shared.application.Command;
import com.whoz_in.domain.device.model.IpAddress;
import com.whoz_in.domain.device.model.MacAddress;
import lombok.Getter;

@Getter
public final class DeviceAdd implements Command {
    private final MacAddress macAddress;
    private final IpAddress ipAddress;
    private final String deviceName;

    public DeviceAdd(String macAddress, String ipAddress, String deviceName) {
        this.macAddress = MacAddress.create(macAddress);
        this.ipAddress = IpAddress.create(ipAddress);
        this.deviceName = deviceName;
    }
}
