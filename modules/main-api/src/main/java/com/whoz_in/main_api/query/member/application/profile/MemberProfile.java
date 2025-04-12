package com.whoz_in.main_api.query.member.application.profile;

import com.whoz_in.main_api.query.shared.application.Response;
import java.util.UUID;

public record MemberProfile(
        UUID memberId,
        int generation,
        String memberName,
        String position,
        String totalActiveTime
) implements Response {
}
