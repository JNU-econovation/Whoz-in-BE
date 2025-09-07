package com.whoz_in.main_api.shared.persistence;

import com.whoz_in.shared.WhozinException;

public class FailDeleteImageException extends WhozinException {
  public static final FailDeleteImageException EXCEPTION = new FailDeleteImageException();
    private FailDeleteImageException() {
        super("6011", "이미지 삭제 실패");
    }
}
