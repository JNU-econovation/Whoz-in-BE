package com.whoz_in.main_api.config.security;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
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
        if(SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthentication authentication){
            return Optional.of(authentication.getPrincipal());
        }

        throw new IllegalStateException("Authentication 타입 오류");
    }
}
