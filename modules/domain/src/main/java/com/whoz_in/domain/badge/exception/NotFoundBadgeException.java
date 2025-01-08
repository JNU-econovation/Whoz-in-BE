package com.whoz_in.domain.badge.exception;

import com.whoz_in.domain.shared.BusinessException;

public class NotFoundBadgeException extends BusinessException {
    public NotFoundBadgeException() {super("5001", "뱃지를 찾을 수 없습니다.");}
}
