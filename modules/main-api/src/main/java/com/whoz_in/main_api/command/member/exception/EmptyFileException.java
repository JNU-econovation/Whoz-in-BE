package com.whoz_in.main_api.command.member.exception;

import com.whoz_in.shared.WhozinException;

public class EmptyFileException extends WhozinException {
    public static final EmptyFileException EXCEPTION = new EmptyFileException();
    public EmptyFileException() {
        super("6003", "빈 파일입니다.");
    }
}
