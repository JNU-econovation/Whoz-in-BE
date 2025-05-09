package com.whoz_in_infra.infra_jpa.domain.feedback;

import com.whoz_in.domain.feedback.FeedbackRepository;
import com.whoz_in.domain.feedback.model.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedbackJpaRepository implements FeedbackRepository {
    private final FeedbackConverter converter;
    private final FeedbackEntityJpaRepository jpaRepository;

    @Override
    public void save(Feedback feedback) {
        FeedbackEntity feedbackEntity = converter.from(feedback);
        jpaRepository.save(feedbackEntity);
    }
}
