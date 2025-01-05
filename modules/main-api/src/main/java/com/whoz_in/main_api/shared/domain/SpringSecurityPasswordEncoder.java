package com.whoz_in.main_api.shared.domain;

import com.whoz_in.domain.member.service.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringSecurityPasswordEncoder implements PasswordEncoder {
    private final org.springframework.security.crypto.password.PasswordEncoder encoder;
    @Override
    public String encode(String plainText) {
        return encoder.encode(plainText);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
