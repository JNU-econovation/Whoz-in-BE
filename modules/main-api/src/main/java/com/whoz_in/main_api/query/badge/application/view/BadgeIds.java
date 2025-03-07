package com.whoz_in.main_api.query.badge.application.view;

import com.whoz_in.main_api.query.badge.application.query.BadgeId;
import com.whoz_in.main_api.query.shared.application.View;
import java.util.Set;

public record BadgeIds(Set<BadgeId> badgeIds) implements View {
}
