package com.whoz_in.domain.badge.exception;

import com.whoz_in.domain.shared.BusinessException;

public class BadgeHideException extends BusinessException {
    public static final BadgeHideException EXCEPTION = new BadgeHideException();
    public BadgeHideException() {super("5006", "숨김 처리된 뱃지입니다.");}
}
