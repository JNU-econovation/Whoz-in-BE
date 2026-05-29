package com.whoz_in.main_api.command.api.member.exception;

import com.whoz_in.shared.WhozinException;

public class ImageProcessingException extends WhozinException {
    public static final ImageProcessingException EXCEPTION = new ImageProcessingException();
    private ImageProcessingException() {
        super("6007", "이미지 변환에 실패하였습니다.");
    }
}
