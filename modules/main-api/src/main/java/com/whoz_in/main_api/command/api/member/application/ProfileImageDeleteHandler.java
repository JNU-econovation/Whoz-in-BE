package com.whoz_in.main_api.command.api.member.application;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.service.MemberFinderService;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.ImageUploadStorage;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class ProfileImageDeleteHandler implements CommandHandler<ProfileImageDelete, Void> {
    private final RequesterInfo requesterInfo;
    private final MemberFinderService memberFinderService;
    private final MemberRepository memberRepository;
    private final ImageUploadStorage imageUploadStorage;


    @Transactional
    @Override
    public Void handle(ProfileImageDelete cmd) {
        MemberId requesterId = requesterInfo.getMemberId();
        Member member = memberFinderService.find(requesterId);
        imageUploadStorage.delete(member.getProfileImageId());
        member.setDefaultProfileImage();
        memberRepository.save(member);
        return null;
    }
}
