package com.whoz_in.main_api.shared.jwt.tokens;

import com.whoz_in.main_api.shared.application.ApplicationException;

public class TokenException extends ApplicationException {
    public TokenException(String errorCode, String message) {
        super(errorCode, message);
    }
}
