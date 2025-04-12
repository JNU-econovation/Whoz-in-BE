package com.whoz_in.main_api.shared.jwt.tokens;

import com.whoz_in.shared.WhozinException;

public class TokenException extends WhozinException {
    public TokenException(String errorCode, String message) {
        super(errorCode, message);
    }
}
