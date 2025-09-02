package com.whoz_in.main_api.command.member.application;

import com.whoz_in.domain.member.ImageRepository;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.service.MemberFinderService;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class UploadProfileImageHandler implements CommandHandler<UploadProfileImage, Void> {
    private final RequesterInfo requesterInfo;
    private final MemberFinderService memberFinderService;
    private final ImageRepository repository;

    @Transactional
    @Override
    public Void handle(UploadProfileImage cmd) {
        MemberId requesterId = requesterInfo.getMemberId();
        Member member = memberFinderService.find(requesterId);
        String imageUrl = repository.save(cmd.bytes());
        member.updateProfileImage(imageUrl);
        return null;
    }
}
