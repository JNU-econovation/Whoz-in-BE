package com.whoz_in.main_api.command.member.application;

import com.whoz_in.main_api.command.shared.application.Command;
import com.whoz_in.main_api.shared.jwt.tokens.RefreshToken;

public record LogOut(
        RefreshToken refreshToken
) implements Command {}
