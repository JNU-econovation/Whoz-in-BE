package com.whoz_in.main_api.command.api.member.exception;

import com.whoz_in.shared.WhozinException;

public class InvalidImageDimensionException extends WhozinException {
  public static final InvalidImageDimensionException EXCEPTION = new InvalidImageDimensionException();
    private InvalidImageDimensionException() {
        super("6008", "이미지 크기가 올바르지 않습니다. (50px ~ 5000px 허용)");
    }
}
