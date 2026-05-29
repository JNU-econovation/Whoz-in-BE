package com.whoz_in.main_api.command.api.member.exception;

import com.whoz_in.shared.WhozinException;

public class InvalidImageFormatException extends WhozinException {
    public static final InvalidImageFormatException EXCEPTION = new InvalidImageFormatException();
    private InvalidImageFormatException() {
        super("6006", "올바르지 않은 이미지 형식입니다.");
    }
}
