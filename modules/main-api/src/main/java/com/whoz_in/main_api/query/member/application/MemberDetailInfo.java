package com.whoz_in.main_api.query.member.application;

import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.application.View;
import java.util.UUID;

public record MemberDetailInfo(
        UUID memberId,
        String name,
        int generation,
        String position,
        String statusMessage

) implements View, Response {}
