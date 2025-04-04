package com.whoz_in.shared.domain_event.member;

import com.whoz_in.shared.domain_event.DomainEvent;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberOAuthInfoRegistered extends DomainEvent {

    private final UUID memberId;
    private final String socialProvider;
    private final String socialId;

}
