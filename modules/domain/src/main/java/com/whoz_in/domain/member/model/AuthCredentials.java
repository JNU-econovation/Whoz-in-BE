package com.whoz_in.domain.member.model;

import com.whoz_in.domain.member.exception.LoginIdPolicyViolationException;
import com.whoz_in.domain.member.exception.PasswordPolicyViolationException;
import com.whoz_in.domain.member.exception.WrongPasswordException;
import com.whoz_in.domain.member.service.PasswordEncoder;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//일반 로그인 정보
//TODO: PasswordEncoder?
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthCredentials {
    private static final String LOGIN_ID_REGEX = "^(?=.*[a-z])(?=.*\\d)[a-zA-Z\\d]{6,16}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*\\d)[a-zA-Z\\d]{6,16}$";
    private final String loginId;
    private final String encodedPassword;

    public static AuthCredentials create(String loginId, String rawPassword, PasswordEncoder passwordEncoder){
        //아이디가 정책에 맞는지 확인
        if (!Pattern.matches(LOGIN_ID_REGEX, loginId))
            throw new LoginIdPolicyViolationException();
        //비밀번호가 정책에 맞는지 확인
        if (!Pattern.matches(PASSWORD_REGEX, rawPassword))
            throw new PasswordPolicyViolationException();
        return new AuthCredentials(loginId, passwordEncoder.encode(rawPassword));
    }

    public static AuthCredentials load(String loginId, String encodedPassword){
        if (loginId == null || encodedPassword == null)
            throw new IllegalStateException("no login id or encoded password");
        return new AuthCredentials(loginId, encodedPassword);
    }

    AuthCredentials changePassword(String rawOldPassword, String rawNewPassword, PasswordEncoder passwordEncoder){
        String encodedOldPassword = passwordEncoder.encode(rawOldPassword);
        //기존 비밀번호가 같은지 확인
        if (!encodedPassword.equals(encodedOldPassword))
            throw new WrongPasswordException();
        //새로운 비밀번호가 정책에 맞는지 확인
        if (!Pattern.matches(PASSWORD_REGEX, rawNewPassword))
            throw new PasswordPolicyViolationException();
        return new AuthCredentials(this.loginId, passwordEncoder.encode(rawNewPassword));
    }
}
