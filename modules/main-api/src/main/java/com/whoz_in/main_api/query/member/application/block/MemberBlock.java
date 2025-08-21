package com.whoz_in.main_api.query.member.application.block;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.presentation.ColonHourMinuteSerializer;
import com.whoz_in.main_api.query.shared.presentation.HourSerializer;
import java.time.Duration;
import java.util.List;

public record MemberBlock(
        List<Block> days,
        @JsonSerialize(using = HourSerializer.class)
        Duration totalActiveTime
) implements Response {
    record Block(
            int day,
            @JsonSerialize(using = ColonHourMinuteSerializer.class)
            Duration activeTime
    ){}
}
