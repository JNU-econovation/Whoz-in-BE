package com.whoz_in.domain.member.domain.exception;

import com.whoz_in.domain.shared.domain.BusinessException;

public class InvalidLoginIdException extends BusinessException {

//  User 관련 1000번대
//  Auth 관련 2000번대
  public InvalidLoginIdException() {
    super("2002", "10자 이상 16자미만, 알파벳 소문자, 특수문자 필수 포함하셔야 합니다.");
  }
}
