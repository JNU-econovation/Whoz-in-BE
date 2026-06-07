package com.whoz_in.main_api.query.open_api.member.application.by_date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.application.View;
import com.whoz_in.main_api.query.shared.presentation.HourMinuteSerializer;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record MembersOnDateView(
        LocalDate date,
        List<MemberPresence> members
) implements View, Response {
    public record MemberPresence(
            UUID memberId,
            int generation,
            String memberName,
            @JsonSerialize(using = HourMinuteSerializer.class)
            Duration presenceDuration
    ) implements View, Response {
    }
}
