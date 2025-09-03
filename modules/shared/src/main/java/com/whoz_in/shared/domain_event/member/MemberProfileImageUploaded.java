package com.whoz_in.shared.domain_event.member;

import com.whoz_in.shared.domain_event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberProfileImageUploaded extends DomainEvent {
    private final String imageId;
}
