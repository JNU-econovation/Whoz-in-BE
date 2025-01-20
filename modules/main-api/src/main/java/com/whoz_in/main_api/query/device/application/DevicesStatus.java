package com.whoz_in.main_api.query.device.application;

import com.whoz_in.domain.shared.Nullable;
import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.application.View;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record DevicesStatus(
        List<DeviceStatus> devices
) implements Response, View {
    public record DeviceStatus(
            UUID deviceId,
            String deviceName,
            Map<String, String> macPerSsid, //Map<SSID, MAC>
            @Nullable String connectedSsid
    ) {}
}

