package com.whoz_in.main_api.shared.utils;

import com.whoz_in.domain.member.model.MemberId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/*
인증인가 구현하는 사람이 구현체를 제공하기 전까지 사용하는 스텁 구현체
 */
@Component
public class StubRequesterInfo implements RequesterInfo {
    @Override
    public Optional<MemberId> findMemberId() {
        return Optional.of(new MemberId(UUID.fromString("00000000-0000-0000-0000-000000000000")));
    }
}
