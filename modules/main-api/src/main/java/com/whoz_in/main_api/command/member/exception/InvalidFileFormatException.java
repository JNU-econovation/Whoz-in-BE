package com.whoz_in.main_api.command.member.exception;

import com.whoz_in.shared.WhozinException;

public class InvalidFileFormatException extends WhozinException {
  public static final InvalidFileFormatException EXCEPTION = new InvalidFileFormatException();
    private InvalidFileFormatException() {
        super("6005", "올바르지 않은 파일 형식입니다.");
    }
}
