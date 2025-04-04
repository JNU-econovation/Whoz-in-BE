package com.whoz_in.domain.badge.exception;

import com.whoz_in.shared.WhozinException;

public class NoBadgeException extends WhozinException {
    public static final NoBadgeException EXCEPTION = new NoBadgeException();
    public NoBadgeException() {super("5001", "뱃지를 찾을 수 없습니다.");}
}
