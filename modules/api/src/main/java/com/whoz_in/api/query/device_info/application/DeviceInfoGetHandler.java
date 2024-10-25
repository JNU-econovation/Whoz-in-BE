package com.whoz_in.api.query.device_info.application;

import com.whoz_in.api.shared.application.Handler;
import com.whoz_in.api.shared.application.query.QueryHandler;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public final class DeviceInfoGetHandler extends QueryHandler<DeviceInfoGet, DeviceInfo> {
    private final DeviceInfoViewer viewer;
    @Override
    public DeviceInfo handle(DeviceInfoGet query) {
        return viewer.findByDeviceId(query.getDeviceId());
    }
}
