package com.whoz_in.main_api.query.open_api.member.application.shared;

import com.whoz_in.main_api.query.shared.application.View;
import java.time.Duration;
import java.util.UUID;

public record DailyActivityView(
        UUID memberId,
        boolean isActive,
        Duration activeTime
) implements View {}
