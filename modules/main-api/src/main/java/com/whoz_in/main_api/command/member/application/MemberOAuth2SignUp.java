package com.whoz_in.main_api.command.member.application;

import com.whoz_in.domain.member.model.Position;
import com.whoz_in.main_api.command.shared.application.Command;

public record MemberOAuth2SignUp(
        String oAuth2TempToken,
        String name,
        Position position,
        Integer generation
) implements Command {}
