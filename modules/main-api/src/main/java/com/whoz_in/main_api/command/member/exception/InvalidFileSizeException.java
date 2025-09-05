package com.whoz_in.main_api.command.member.exception;

import com.whoz_in.shared.WhozinException;

public class InvalidFileSizeException extends WhozinException {
    public static final InvalidFileSizeException EXCEPTION = new InvalidFileSizeException();
    public InvalidFileSizeException() {
        super("6004", "파일 크기는 최소 1KB이상 최대 2MB까지만 가능합니다.");
    }
}
