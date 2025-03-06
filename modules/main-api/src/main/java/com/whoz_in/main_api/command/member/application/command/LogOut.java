package com.whoz_in.main_api.command.member.application.command;

import com.whoz_in.main_api.command.shared.application.Command;

public record LogOut(
        String memberId,
        String accessTokenId,
        String refreshTokenId
) implements Command {
}
