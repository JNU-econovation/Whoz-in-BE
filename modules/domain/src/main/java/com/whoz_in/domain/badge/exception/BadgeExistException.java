package com.whoz_in.domain.badge.exception;

import com.whoz_in.shared.WhozinException;

public class BadgeExistException extends WhozinException {
  public static final BadgeExistException EXCEPTION = new BadgeExistException();
  public BadgeExistException() {
    super("5005", "존재하는 뱃지입니다.");
  }
}
