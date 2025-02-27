package com.whoz_in.main_api.command.feedback.application;

import com.whoz_in.main_api.command.shared.application.Command;

public record FeedbackSend(
        String content
) implements Command {
}
