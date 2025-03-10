package com.whoz_in.domain.feedback.model;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.shared.AggregateRoot;
import com.whoz_in.domain.feedback.event.FeedbackCreated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access =  AccessLevel.PRIVATE)
public final class Feedback extends AggregateRoot {
    private final FeedbackId id;
    private final String title;
    private final String content;
    private final MemberId writer;

    public static Feedback create(String title, String content, MemberId writer) {
        Feedback feedback = Feedback.builder()
                .id(new FeedbackId())
                .title(title)
                .content(content)
                .writer(writer)
                .build();
        feedback.register(new FeedbackCreated(title,content,writer.id().toString()));
        return feedback;
    }

    public static Feedback load(FeedbackId id, String title, String content, MemberId writer) {
        return Feedback.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .build();
    }
}
