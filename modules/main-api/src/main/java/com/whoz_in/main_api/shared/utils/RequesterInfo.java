package com.whoz_in.main_api.shared.utils;

import com.whoz_in.domain.member.model.MemberId;
import java.util.Optional;

/*
요청에서 유저의 아이디를 가져오는 기능
인증인가를 구현하는 사람은 스프링 빈으로 구현체를 제공하여야 한다.
 */

public interface RequesterInfo {
    Optional<MemberId> findMemberId();
    default MemberId getMemberId(){
        return findMemberId().orElseThrow(UserNotAuthenticationException::new);
    }
}