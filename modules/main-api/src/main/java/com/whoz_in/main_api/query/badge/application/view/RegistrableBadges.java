package com.whoz_in.main_api.query.badge.application.view;

import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.application.View;
import java.util.List;
import java.util.UUID;

public record RegistrableBadges(List<RegistrableBadge> registerableBadge)
        implements Response, View {
    public record RegistrableBadge(
            UUID badgeId
    ) {}
}
