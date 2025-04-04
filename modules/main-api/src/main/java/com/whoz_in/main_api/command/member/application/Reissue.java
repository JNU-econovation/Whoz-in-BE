package com.whoz_in.main_api.command.member.application;

import com.whoz_in.main_api.command.shared.application.Command;

public record Reissue(
    String memberId,
    String refreshTokenId
) implements Command {
}
