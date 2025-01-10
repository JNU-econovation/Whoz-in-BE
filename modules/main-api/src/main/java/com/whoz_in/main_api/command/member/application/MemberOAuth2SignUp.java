package com.whoz_in.main_api.command.member.application;

import com.whoz_in.domain.member.model.Position;
import com.whoz_in.domain.member.model.SocialProvider;
import com.whoz_in.main_api.command.shared.application.Command;

public record MemberOAuth2SignUp(
        SocialProvider socialProvider,
        String socialId,
        String name,
        Position position,
        Integer generation
) implements Command {}
