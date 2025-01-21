package com.whoz_in.main_api.query.member.application;

import com.whoz_in.main_api.query.shared.application.View;
import java.time.Duration;
import java.util.UUID;

public record MemberConnectionInfo(
        UUID memberId,
        Duration dailyTime,
        Duration totalTime,
        boolean isActive
    ) implements View {
}
