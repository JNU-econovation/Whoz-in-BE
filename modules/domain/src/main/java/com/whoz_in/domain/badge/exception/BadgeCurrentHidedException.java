package com.whoz_in.domain.badge.exception;

import com.whoz_in.domain.shared.BusinessException;

public class BadgeCurrentHidedException extends BusinessException {
    public static final BadgeCurrentHidedException EXCEPTION = new BadgeCurrentHidedException();
    public BadgeCurrentHidedException() {super("5006", "숨김 처리된 뱃지입니다.");}
}
