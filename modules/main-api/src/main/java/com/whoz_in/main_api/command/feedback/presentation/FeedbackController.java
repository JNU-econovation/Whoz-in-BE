package com.whoz_in.main_api.command.feedback.presentation;

import com.whoz_in.main_api.command.feedback.application.FeedbackSend;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.command.shared.presentation.CommandController;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedbackController extends CommandController {
    public FeedbackController(CommandBus commandBus) {super(commandBus);}

    @PostMapping("/api/v1/feedback")
    public ResponseEntity<SuccessBody<Void>> send(@RequestBody FeedbackSend request) {
        dispatch(request);
        return ResponseEntityGenerator.success("피드백 보내기 완료", HttpStatus.CREATED);
    }
}
