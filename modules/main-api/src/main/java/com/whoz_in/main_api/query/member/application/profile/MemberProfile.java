package com.whoz_in.main_api.query.member.application.profile;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.presentation.DurationToHourSerializer;
import java.time.Duration;
import java.util.UUID;

public record MemberProfile(
        UUID memberId,
        int generation,
        String memberName,
        String position,
        @JsonSerialize(using = DurationToHourSerializer.class)
        Duration totalActiveTime
) implements Response {
}
