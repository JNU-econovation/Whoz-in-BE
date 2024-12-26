package com.whoz_in.main_api.command.member.application;

import com.whoz_in.domain.member.model.Position;
import com.whoz_in.main_api.shared.application.command.Command;

public record MemberSignUp(
        String loginId,
        String password,
        String name,
        Position position,
        Integer generation
) implements Command {}
