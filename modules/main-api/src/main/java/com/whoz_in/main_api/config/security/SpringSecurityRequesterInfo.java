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

        // TODO: 현재 서버 간 통신은 처리하지 않고 있음. 새로운 Authentication 만들어야 할듯
        return Optional.empty();
    }
}
