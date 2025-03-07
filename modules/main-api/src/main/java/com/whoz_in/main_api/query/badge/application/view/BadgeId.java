package com.whoz_in.main_api.query.badge.application.view;

import com.whoz_in.main_api.query.shared.application.View;
import java.util.UUID;

public record BadgeId(UUID badgeId) implements View {
}
