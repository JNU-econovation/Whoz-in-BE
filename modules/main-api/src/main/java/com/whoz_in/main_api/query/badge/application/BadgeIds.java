package com.whoz_in.main_api.query.badge.application;

import com.whoz_in.main_api.query.shared.application.View;
import java.util.Set;
import lombok.Getter;

public record BadgeIds(Set<BadgeId> badgeIds) implements View {
}
