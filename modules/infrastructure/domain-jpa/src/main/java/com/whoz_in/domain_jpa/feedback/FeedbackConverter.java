package com.whoz_in.domain_jpa.feedback;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.feedback.model.Feedback;
import com.whoz_in.domain.feedback.model.FeedbackId;
import com.whoz_in.domain_jpa.shared.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class FeedbackConverter extends BaseConverter<FeedbackEntity, Feedback> {
    @Override
    public FeedbackEntity from(Feedback feedback) {
        return new FeedbackEntity(
                feedback.getId().id(),
                feedback.getContent(),
                feedback.getWriter().id()
        );

    }

    @Override
    public Feedback to(FeedbackEntity entity) {
        return Feedback.load(
                new FeedbackId(entity.getId()),
                entity.getContent(),
                new MemberId(entity.getMemberId())
        );
    }

}
