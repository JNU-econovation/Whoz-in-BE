package com.whoz_in.domain.member.domain.exception;

import com.whoz_in.domain.shared.domain.BusinessException;

public class InvalidPasswordException extends BusinessException {

  //User 관련 1000번대
  //Auth 관련 2000번대
  public InvalidPasswordException() {
    super("2001", "8자 이상 16자미만, 알파벳 소문자, 숫자로 조합하여 구성하십시오");
  }
}
