package com.whoz_in.domain.feedback;

import com.whoz_in.domain.feedback.model.Feedback;

public interface FeedbackRepository {
    void save(Feedback feedback);
}
