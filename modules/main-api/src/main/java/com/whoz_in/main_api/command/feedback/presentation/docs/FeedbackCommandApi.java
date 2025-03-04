package com.whoz_in.main_api.command.feedback.presentation.docs;

import com.whoz_in.main_api.command.feedback.application.FeedbackSend;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Feedback", description = "Feedback Api")
public interface FeedbackCommandApi {

    @Operation(
            summary = "Feedback 보내기",
            description = "사용자로부터 Feedback 받습니다."
    )
    ResponseEntity<SuccessBody<Void>> send(@RequestBody FeedbackSend request);

}
