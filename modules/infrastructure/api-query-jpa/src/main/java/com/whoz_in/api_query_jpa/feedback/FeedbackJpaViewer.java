package com.whoz_in.api_query_jpa.feedback;

import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import com.whoz_in.main_api.query.feedback.view.FeedbackInfo;
import com.whoz_in.main_api.query.feedback.view.FeedbackViewer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedbackJpaViewer implements FeedbackViewer {

    private final FeedbackRepository repository;
    private final MemberRepository memberRepository;

    @Override
    public List<FeedbackInfo> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toFeedbackInfo)
                .toList();
    }

    private FeedbackInfo toFeedbackInfo(Feedback feedback) {
        Member member = memberRepository.findById(feedback.getMemberId()).get();
        return FeedbackInfo.builder()
                .memberName(member.getName())
                .title(feedback.getTitle())
                .content(feedback.getContent())
                .build();
    }

}
