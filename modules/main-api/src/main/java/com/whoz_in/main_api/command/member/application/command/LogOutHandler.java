package com.whoz_in.main_api.command.member.application.command;

import com.whoz_in.main_api.shared.jwt.tokens.RefreshTokenStore;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class LogOutHandler implements CommandHandler<LogOut, Void> {

    private final RefreshTokenStore refreshTokenStore;

    @Override
    public Void handle(LogOut command) {

        String refreshTokenId = command.refreshTokenId();
        String accessTokenId = command.accessTokenId();

        refreshTokenStore.save(refreshTokenId);

        return null;
    }
}
