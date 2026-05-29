package com.whoz_in.main_api.query.api.member.application.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.presentation.HourSerializer;
import java.time.Duration;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberProfile(
        UUID memberId,
        int generation,
        String memberName,
        String position,
        @JsonSerialize(using = HourSerializer.class)
        Duration totalActiveTime,
        String profileImageUrl
) implements Response {
}
