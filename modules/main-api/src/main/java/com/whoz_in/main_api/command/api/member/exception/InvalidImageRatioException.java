package com.whoz_in.main_api.command.api.member.exception;

import com.whoz_in.shared.WhozinException;

public class InvalidImageRatioException extends WhozinException {
    public static final InvalidImageRatioException EXCEPTION = new InvalidImageRatioException();
    private InvalidImageRatioException() {
        super("6009", "이미지 비율이 적절치 않습니다. (최대 10:1)");
    }
}
