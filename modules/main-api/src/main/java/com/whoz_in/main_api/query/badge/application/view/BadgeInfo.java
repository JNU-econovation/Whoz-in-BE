package com.whoz_in.main_api.query.badge.application.view;

import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.application.View;

public record BadgeInfo(
        String name,
        String colorCode
) implements View, Response {
}
