package com.whoz_in.main_api.command.member.application;

import com.whoz_in.main_api.command.shared.application.Command;

public record MemberOAuth2Login(
        String socialId
) implements Command {}
