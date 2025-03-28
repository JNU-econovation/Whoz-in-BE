package com.whoz_in.api_query_jpa.feedback;

import com.whoz_in.main_api.query.feedback.view.FeedbackInfo;
import com.whoz_in.main_api.query.feedback.view.FeedbackViewer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedbackJpaViewer implements FeedbackViewer {

    private final FeedbackRepository repository;

    @Override
    public List<FeedbackInfo> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toFeedbackInfo)
                .toList();
    }

    private FeedbackInfo toFeedbackInfo(Feedback feedback) {
        return FeedbackInfo.builder()
                .id(feedback.getId())
                .title(feedback.getTitle())
                .content(feedback.getContent())
                .build();
    }

}
