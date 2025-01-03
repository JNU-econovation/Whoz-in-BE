package com.whoz_in.domain.member.event;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.model.OAuthCredentials;
import com.whoz_in.domain.shared.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberOAuthInfoRegistered extends DomainEvent {

    private final MemberId aggregateId;
    private final OAuthCredentials oAuthCredentials;

}
