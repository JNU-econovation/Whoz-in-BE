package com.whoz_in.main_api.shared.utils;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.config.security.oauth2.JwtAuthentication;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/*
인증인가 구현하는 사람이 구현체를 제공하기 전까지 사용하는 스텁 구현체
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
            throw new IllegalStateException(e.getMessage());
        }

        return Optional.of(authentication.getPrincipal());
    }
}
