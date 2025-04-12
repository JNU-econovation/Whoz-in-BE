package com.whoz_in.shared.domain_event.feedback;

import com.whoz_in.shared.domain_event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class FeedbackCreated extends DomainEvent {
    private final String title;
    private final String content;
    private final String memberId;
}
