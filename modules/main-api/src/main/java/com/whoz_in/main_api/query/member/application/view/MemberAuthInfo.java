package com.whoz_in.main_api.query.member.application.view;

import com.whoz_in.main_api.query.shared.application.View;
import java.util.UUID;

public record MemberAuthInfo(
        UUID memberId,
        String loginId,
        String encodedPassword
) implements View {}
