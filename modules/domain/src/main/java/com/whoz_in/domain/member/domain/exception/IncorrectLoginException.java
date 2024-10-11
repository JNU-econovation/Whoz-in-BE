package com.whoz_in.domain.member.domain.exception;

import com.whoz_in.domain.shared.domain.BusinessException;

public class IncorrectLoginException extends BusinessException {

  public IncorrectLoginException() {
    super("2003", "아이디 혹은 비밀번호가 잘못되었습니다.");
  }
}
