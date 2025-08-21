package com.whoz_in.main_api.command.member.application;

import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.jwt.tokens.RefreshTokenStore;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class LogOutHandler implements CommandHandler<LogOut, Void> {

    private final RefreshTokenStore refreshTokenStore;

    @Override
    public Void handle(LogOut command) {
        refreshTokenStore.saveIfAbsent(command.refreshToken());
        return null;
    }
}
