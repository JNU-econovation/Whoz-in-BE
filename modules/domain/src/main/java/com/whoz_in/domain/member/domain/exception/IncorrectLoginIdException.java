package com.whoz_in.domain.member.domain.exception;

import com.whoz_in.domain.shared.domain.BusinessException;

public class IncorrectLoginIdException extends BusinessException {

  public IncorrectLoginIdException() {
    super("2003","아이디가 일치하지 않습니다.");
  }
}
