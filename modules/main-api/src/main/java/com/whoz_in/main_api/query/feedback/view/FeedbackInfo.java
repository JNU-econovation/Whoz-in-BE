package com.whoz_in.main_api.query.feedback.view;

import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.application.View;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record FeedbackInfo(
    String memberName,
    String title,
    String content,
    LocalDateTime createdAt
)
        implements View, Response {
}
