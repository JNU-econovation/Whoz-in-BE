package com.whoz_in.main_api.command.member.application.token;

import org.springframework.stereotype.Component;

@Component
public class StubRefreshTokenStore implements RefreshTokenStore{

    @Override
    public void save(String refreshTokenId) {
    }
}
