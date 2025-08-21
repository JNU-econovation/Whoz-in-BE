package com.whoz_in_infra.infra_jpa.domain.feedback;

import com.whoz_in.domain.feedback.model.Feedback;
import com.whoz_in.domain.feedback.model.FeedbackId;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in_infra.infra_jpa.domain.shared.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class FeedbackConverter extends BaseConverter<FeedbackEntity, Feedback> {
    @Override
    public FeedbackEntity from(Feedback feedback) {
        return new FeedbackEntity(
                feedback.getId().id(),
                feedback.getTitle(),
                feedback.getContent(),
                feedback.getWriter().id()
        );

    }

    @Override
    public Feedback to(FeedbackEntity entity) {
        return Feedback.load(
                new FeedbackId(entity.getId()),
                entity.getTitle(),
                entity.getContent(),
                new MemberId(entity.getMemberId())
        );
    }

}
