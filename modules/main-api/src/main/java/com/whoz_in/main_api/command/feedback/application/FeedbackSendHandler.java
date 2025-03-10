package com.whoz_in.main_api.command.feedback.application;

import com.whoz_in.domain.feedback.FeedbackRepository;
import com.whoz_in.domain.feedback.model.Feedback;
import com.whoz_in.domain.shared.event.EventBus;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class FeedbackSendHandler implements CommandHandler<FeedbackSend, Void> {
    private final FeedbackRepository repository;
    private final EventBus eventBus;
    private final RequesterInfo requesterInfo;

    @Transactional
    @Override
    public Void handle(FeedbackSend cmd) {
        Feedback feedback = Feedback.create(
                cmd.title(),
                cmd.content(),
                requesterInfo.getMemberId()
        );
        repository.save(feedback);
        eventBus.publish(feedback.pullDomainEvents());
        return null;
    }
}
