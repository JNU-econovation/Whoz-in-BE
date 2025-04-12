package com.whoz_in.main_api.query.member.application.shared;

import com.whoz_in.main_api.query.shared.application.View;
import java.time.Duration;
import java.util.UUID;

public record MemberActivityView(
        UUID memberId,
        boolean isActive,
        Duration activeTime

        // activeRoom, lastDisconnectedAt, lastDisconnectRoom 등 추가 가능
) implements View {}
