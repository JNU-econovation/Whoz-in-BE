package com.whoz_in.main_api.query.member.application;

import com.whoz_in.main_api.shared.application.query.Query;

public record MemberLogin(
        String loginId,
        String password
) implements Query {}

