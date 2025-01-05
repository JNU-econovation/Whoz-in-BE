package com.whoz_in.main_api.query.device.application;

import com.whoz_in.main_api.query.shared.application.Response;

public record ActiveDeviceResponse(
    String deviceId,
    String memberId,
    String memberName,
    String continuousActiveTime,
    String totalActiveTime
) implements Response {
}
