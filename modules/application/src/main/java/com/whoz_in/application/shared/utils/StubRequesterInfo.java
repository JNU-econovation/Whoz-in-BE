package com.whoz_in.application.shared.utils;

import java.util.Optional;
import org.springframework.stereotype.Component;

/*
인증인가 구현하는 사람이 구현체를 제공하기 전까지 사용하는 스텁 구현체
 */
@Component
public class StubRequesterInfo implements RequesterInfo {
    @Override
    public Optional<Long> findUserId() {
        return Optional.of(1L);
    }
}
