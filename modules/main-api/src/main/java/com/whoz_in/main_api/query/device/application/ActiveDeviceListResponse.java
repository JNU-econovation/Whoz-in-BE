package com.whoz_in.main_api.query.device.application;

import com.whoz_in.main_api.query.shared.application.Response;
import java.util.List;

public record ActiveDeviceListResponse(
        List<ActiveDeviceResponse> responses
) implements Response {
}
