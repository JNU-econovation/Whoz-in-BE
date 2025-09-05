package com.whoz_in.main_api.command.member.application;

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
public class UploadProfileImageHandler implements CommandHandler<UploadProfileImage, Void> {
    private final RequesterInfo requesterInfo;
    private final MemberFinderService memberFinderService;
    private final ImageUploadStorage imageUploadStorage;


    @Transactional
    @Override
    public Void handle(UploadProfileImage cmd) {
        MemberId requesterId = requesterInfo.getMemberId();
        Member member = memberFinderService.find(requesterId);
        imageUploadStorage.save(member.getImageId(), cmd.bytes());
        return null;
    }
}
