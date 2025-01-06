package com.whoz_in.main_api.command.member.application;

public record LoginSuccessTokens(
        String accessToken,
        String refreshToken
) {}
