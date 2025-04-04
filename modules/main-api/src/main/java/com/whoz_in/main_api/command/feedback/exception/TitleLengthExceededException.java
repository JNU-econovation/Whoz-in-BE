package com.whoz_in.main_api.command.feedback.exception;

import com.whoz_in.shared.WhozinException;

public class TitleLengthExceededException extends WhozinException {
  public static final TitleLengthExceededException EXCEPTION = new TitleLengthExceededException();
    public TitleLengthExceededException() {
        super(
                "5001", "제목은 30자까지 작성가능합니다."
        );
    }
}
