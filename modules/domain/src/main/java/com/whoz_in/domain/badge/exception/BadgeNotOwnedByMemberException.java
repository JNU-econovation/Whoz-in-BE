package com.whoz_in.domain.badge.exception;

import com.whoz_in.shared.WhozinException;

public class BadgeNotOwnedByMemberException extends WhozinException {
    public static final BadgeNotOwnedByMemberException EXCEPTION = new BadgeNotOwnedByMemberException();
    private BadgeNotOwnedByMemberException() {super("5007", "가지고 있지 않은 뱃지입니다.");}
}
