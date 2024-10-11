package com.whoz_in.domain.member.domain.exception;

import com.whoz_in.domain.shared.domain.BusinessException;

public class IncorrectPasswordException extends BusinessException {

  public IncorrectPasswordException() {
    super("2004", "비밀번호가 일치하지 않습니다.");
  }
}
