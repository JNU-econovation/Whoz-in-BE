package com.whoz_in.main_api.command.api.member.application;

public record LoginSuccessTokens(
        String accessToken,
        String refreshToken
) {}
