package com.whoz_in.main_api.query.badge.application.response;

import com.whoz_in.main_api.query.badge.application.query.BadgeId;
import com.whoz_in.main_api.query.shared.application.Response;
import java.util.Set;

public record RegisterableBadgeResponse(Set<BadgeId> badgeIds) implements Response {
}
