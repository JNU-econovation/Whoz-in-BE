package com.whoz_in.main_api.query.badge.application.response;

import com.whoz_in.main_api.query.shared.application.Response;

public record BadgeInfoResponse(
        String name,
        String colorCode
) implements Response {
}
