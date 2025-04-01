package com.whoz_in.main_api.query.member.application.view;

import com.whoz_in.main_api.query.badge.application.view.BadgeInfo;
import com.whoz_in.main_api.query.shared.application.View;
import java.util.List;
import java.util.UUID;

public record MemberInfo(
        UUID memberId,
        String position,
        String statusMessage,
        int generation,
        String memberName,
        List<BadgeInfo> badges,
        BadgeInfo repBadge
) implements View {

}
