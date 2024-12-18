package com.whoz_in.main_api.query.device_info.application;

import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.application.query.QueryHandler;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public final class DevicesInfoGetHandler extends QueryHandler<DevicesInfoGet, DevicesInfo> {
    private final DeviceInfoViewer viewer;
    @Override
    public DevicesInfo handle(DevicesInfoGet query) {
        return viewer.findAll();
    }
}
