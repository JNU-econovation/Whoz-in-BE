package com.whoz_in.main_api.shared.utils;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.config.security.oauth2.JwtAuthentication;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/*
SpringSecurity 를 이용한 RequesterInfo
 */
@Component
@RequiredArgsConstructor
public class SpringSecurityRequesterInfo implements RequesterInfo {

    @Override
    public Optional<MemberId> findMemberId() {
        JwtAuthentication authentication;
        try {
            authentication = (JwtAuthentication) SecurityContextHolder.getContext()
                    .getAuthentication(); // 이 결과가 null 일 경우
        } catch (ClassCastException e){
            throw new IllegalStateException("Authentication 타입 오류");
        }

        return Optional.of(authentication.getPrincipal());
    }
}
