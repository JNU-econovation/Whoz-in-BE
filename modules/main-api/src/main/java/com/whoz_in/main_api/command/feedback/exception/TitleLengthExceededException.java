package com.whoz_in.main_api.command.feedback.exception;

import com.whoz_in.main_api.shared.application.ApplicationException;

public class TitleLengthExceededException extends ApplicationException {
  public static final TitleLengthExceededException EXCEPTION = new TitleLengthExceededException();
    public TitleLengthExceededException() {
        super(
                "5001", "제목은 50자까지 작성가능합니다."
        );
    }
}
