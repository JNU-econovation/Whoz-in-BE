package com.whoz_in.main_api.query.member.application.exception;

import com.whoz_in.shared.WhozinException;

public class NotFoundConnectionInfoException extends WhozinException {

    public NotFoundConnectionInfoException() {
        super("6001", "MemberConnection must be non-null");
    }
}
