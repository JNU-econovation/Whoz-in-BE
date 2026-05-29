package com.whoz_in.main_api.query.api.member.application.detail;

import com.whoz_in.main_api.query.shared.application.Response;
import java.util.UUID;

public record MemberDetail(
        UUID memberId,
        int generation,
        String name,
        String position,
        String statusMessage,
        String profileImageUrl
) implements Response {}
