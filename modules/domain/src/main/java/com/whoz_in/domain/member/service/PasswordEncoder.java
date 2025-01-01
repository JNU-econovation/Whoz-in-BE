package com.whoz_in.domain.member.service;

public interface PasswordEncoder {
    String encode(String plainText);
    boolean matches(String rawPassword, String encodedPassword);
}
