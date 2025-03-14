package com.whoz_in.main_api.command.feedback.application;

import com.whoz_in.domain.shared.Nullable;
import com.whoz_in.main_api.command.feedback.exception.ContentLengthExceedException;
import com.whoz_in.main_api.command.feedback.exception.TitleLengthExceededException;
import com.whoz_in.main_api.command.shared.application.Command;

public record FeedbackSend(
        String title,
        String content
) implements Command {
    public static final int MAX_LENGTH_TITLE = 30;
    public static final int MAX_LENGTH_CONTENT = 500;

    public FeedbackSend {
        if (title != null && title.length() > MAX_LENGTH_TITLE) {
            throw TitleLengthExceededException.EXCEPTION;
        }
        if (content != null && content.length() > MAX_LENGTH_CONTENT) {
            throw ContentLengthExceedException.EXCEPTION;
        }
    }
}
