package com.whoz_in.domain_jpa.feedback;

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
