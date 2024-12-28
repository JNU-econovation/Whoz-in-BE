package com.whoz_in.main_api.query.device_info.application;

import com.whoz_in.main_api.query.shared.application.Query;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class DeviceInfoGet implements Query {
    private final Long deviceId;
}
