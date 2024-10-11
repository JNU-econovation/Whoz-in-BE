package com.whoz_in.domain.member.domain.model;

import com.whoz_in.domain.member.domain.exception.IncorrectLoginIdException;
import com.whoz_in.domain.member.domain.exception.IncorrectPasswordException;
import com.whoz_in.domain.member.domain.exception.InvalidLoginIdException;
import com.whoz_in.domain.member.domain.exception.InvalidPasswordException;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthInfo {
  private final String loginId;
  private final String password;

  // 아이디 형식이 맞는지 검증하는 패턴
  private static final Pattern patternId = Pattern.compile(
          "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{8,16}$"
  );

  // 비밀번호 형식이 맞는지 검증하는 패턴
  private static final Pattern patternPassword = Pattern.compile(
          "^(?=.*[a-z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])[a-z!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{10,16}$"
  );

  private static boolean isValidId(String loginId) { return patternId.matcher(loginId).matches(); }
  private static boolean isValidPassword(String password) { return patternPassword.matcher(password).matches(); }


  public void sameCredential(String userLoginId, String userPassword) {
    if (!userLoginId.equals(loginId)) {
      throw new IncorrectLoginIdException();
    }
    if (!userPassword.equals(password)) {
      throw new IncorrectPasswordException();
    }

  }

  // 팩토리 메서드 create: 사용자로부터 입력을 받아 새로 생성하는 경우
  public static AuthInfo create(String userLoginId, String userPassword) {
    if (!isValidId(userLoginId)) {
      throw new InvalidLoginIdException();
    }
    if (!isValidPassword(userPassword)) {
      throw new InvalidPasswordException();
    }
    //sameCredential(userLoginId, userPassword);

    return new AuthInfo(userLoginId, userPassword);
  }

  // 팩토리 메서드 load: db로부터 가져오는 경우
  static AuthInfo load(String loginId, String password) { return new AuthInfo(loginId, password); }
}
