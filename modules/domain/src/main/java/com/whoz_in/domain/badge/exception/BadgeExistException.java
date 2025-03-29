package com.whoz_in.domain.badge.exception;

import com.whoz_in.domain.shared.BusinessException;

public class BadgeExistException extends BusinessException {
  public static final BadgeExistException EXCEPTION = new BadgeExistException();
  public BadgeExistException() {
    super("5005", "존재하는 뱃지입니다.");
  }
}
