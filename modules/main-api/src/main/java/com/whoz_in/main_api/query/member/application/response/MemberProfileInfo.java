package com.whoz_in.main_api.query.member.application.response;

import com.whoz_in.domain.member.model.Position;
import com.whoz_in.main_api.query.shared.application.Response;

public record MemberProfileInfo(
        String memberId,
        int generation,
        String memberName,
        Position position,
        String totalActiveTime
) implements Response {
}
