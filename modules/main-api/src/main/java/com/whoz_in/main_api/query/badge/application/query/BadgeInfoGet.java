package com.whoz_in.main_api.query.badge.application.query;

import com.whoz_in.main_api.query.shared.application.Query;
import java.util.UUID;

public record BadgeInfoGet(
        UUID badgeId
) implements Query {
}
