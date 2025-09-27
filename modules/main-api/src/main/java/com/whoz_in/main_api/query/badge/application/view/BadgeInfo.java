package com.whoz_in.main_api.query.badge.application.view;

import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.application.View;

public record BadgeInfo(
        String name,
        String colorString,
        String description
) implements View, Response {
}
