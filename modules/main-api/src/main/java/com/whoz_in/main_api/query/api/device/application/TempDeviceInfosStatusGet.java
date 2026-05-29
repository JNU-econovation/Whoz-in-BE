package com.whoz_in.main_api.query.api.device.application;

import com.whoz_in.main_api.query.shared.application.Query;

public record TempDeviceInfosStatusGet(
        String room,
        String ip
) implements Query {}
