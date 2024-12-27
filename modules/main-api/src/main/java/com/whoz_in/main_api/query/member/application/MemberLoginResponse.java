package com.whoz_in.main_api.query.member.application;

import com.whoz_in.main_api.shared.application.query.Response;
import java.util.UUID;

public record MemberLoginResponse(
        UUID memberId
) implements Response {}
