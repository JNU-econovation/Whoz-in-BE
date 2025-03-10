package com.whoz_in.main_api.command.feedback.exception;

import com.whoz_in.main_api.shared.application.ApplicationException;

public class ContentLengthExceedException extends ApplicationException {
    public static final ContentLengthExceedException EXCEPTION = new ContentLengthExceedException();
    public ContentLengthExceedException() {
        super(
                "5002", "본문 내용은 500자까지만 작성 가능합니다."
        );
    }
}
