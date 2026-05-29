package com.whoz_in.main_api.command.api.member.exception;

import com.whoz_in.shared.WhozinException;

public class EmptyFileException extends WhozinException {
    public static final EmptyFileException EXCEPTION = new EmptyFileException();
    private EmptyFileException() {
        super("6003", "빈 파일입니다.");
    }
}
