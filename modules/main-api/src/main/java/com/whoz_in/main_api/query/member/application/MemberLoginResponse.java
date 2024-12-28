package com.whoz_in.main_api.query.member.application;

import com.whoz_in.main_api.query.shared.application.Response;
import java.util.UUID;

public record MemberLoginResponse(
        UUID memberId
) implements Response {}
