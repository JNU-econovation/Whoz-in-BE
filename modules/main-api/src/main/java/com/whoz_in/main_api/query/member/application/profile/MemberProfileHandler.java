package com.whoz_in.main_api.query.member.application.profile;

import com.whoz_in.domain.member.exception.NoMemberException;
import com.whoz_in.main_api.query.member.application.shared.MemberInfoViewer;
import com.whoz_in.main_api.query.member.application.shared.MemberInfoView;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.ProfileImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Handler
@RequiredArgsConstructor
@Slf4j
public class MemberProfileHandler implements QueryHandler<MemberProfileGet, MemberProfile> {

    private final MemberInfoViewer memberInfoViewer;
    private final ProfileImageService profileImageService;

    @Override
    public MemberProfile handle(MemberProfileGet query) {
        MemberInfoView memberInfoView = memberInfoViewer.findByMemberId(query.memberId())
                .orElseThrow(() -> {
                    log.warn("회원 정보 없음: memberId={}", query.memberId());
                    return NoMemberException.EXCEPTION;
                });

        // url 생성 로직이 S3 등으로 변경되면 ProfileImageService를 수정한다.
        String profileImageUrl = profileImageService.getProfileImageUrl(memberInfoView.profileImageId().toString());

        return new MemberProfile(
                memberInfoView.memberId(),
                memberInfoView.generation(),
                memberInfoView.name(),
                memberInfoView.position(),
                memberInfoView.totalActiveTime(),
                profileImageUrl
        );
    }
}
