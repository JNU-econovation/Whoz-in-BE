package com.whoz_in.domain.badge.exception;

import com.whoz_in.shared.WhozinException;

public class AlreadyHasBadgeException extends WhozinException {
  public static final AlreadyHasBadgeException EXCEPTION = new AlreadyHasBadgeException();
  public AlreadyHasBadgeException() {
    super("5004", "동일한 뱃지를 가지고 있습니다.");
  }
}
