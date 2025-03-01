package com.whoz_in.domain.feedback.event;

import com.whoz_in.domain.shared.event.DomainEvent;
import com.whoz_in.domain.feedback.model.Feedback;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class FeedbackCreated extends DomainEvent {
    private final Feedback feedback;
}
