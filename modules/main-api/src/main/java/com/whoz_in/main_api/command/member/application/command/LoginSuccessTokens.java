package com.whoz_in.main_api.command.member.application.command;

public record LoginSuccessTokens(
        String accessToken,
        String refreshToken
) {}
