package com.whoz_in.main_api.query.feedback.view;

import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.query.shared.application.View;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record FeedbackInfo(
    UUID id,
    String title,
    String content,
    LocalDateTime createdAt
)
        implements View, Response {
}
