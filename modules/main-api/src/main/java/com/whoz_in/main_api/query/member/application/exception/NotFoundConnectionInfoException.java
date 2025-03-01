package com.whoz_in.main_api.query.member.application.exception;

import com.whoz_in.main_api.shared.application.ApplicationException;

public class NotFoundConnectionInfoException extends ApplicationException {

    public NotFoundConnectionInfoException() {
        super("6001", "MemberConnection must be non-null");
    }
}
