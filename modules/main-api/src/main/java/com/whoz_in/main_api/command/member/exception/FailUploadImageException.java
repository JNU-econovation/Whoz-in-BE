package com.whoz_in.main_api.command.member.exception;

import com.whoz_in.shared.WhozinException;

public class FailUploadImageException extends WhozinException {
    public static final FailUploadImageException EXCEPTION = new FailUploadImageException();
    public FailUploadImageException() {
        super("6002", "프로필 이미지를 업로드하는데 실패하였습니다.");
    }
}
