package com.whoz_in.main_api.query.member.application;

import com.whoz_in.main_api.shared.application.query.View;
import java.util.UUID;

public record MemberAuthInfo(
        UUID memberId,
        String loginId,
        String encodedPassword
) implements View {}
