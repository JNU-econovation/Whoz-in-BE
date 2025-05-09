package com.whoz_in.main_api.query.member.application.shared;

import com.whoz_in.main_api.query.shared.application.View;
import java.time.Duration;
import java.util.UUID;

public record MemberInfoView(
        UUID memberId,
        int generation,
        String name,
        String position,
        String statusMessage,
        Duration totalActiveTime,
        String mainBadgeName,
        String mainBadgeColor
) implements View {

}
