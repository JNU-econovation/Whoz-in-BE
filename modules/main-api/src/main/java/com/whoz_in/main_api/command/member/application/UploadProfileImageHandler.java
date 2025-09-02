package com.whoz_in.main_api.command.member.application;

import com.whoz_in.domain.image.ImageRepository;
import com.whoz_in.domain.image.model.Image;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.service.MemberFinderService;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class UploadProfileImageHandler implements CommandHandler<UploadProfileImage, Void> {
    private final RequesterInfo requesterInfo;
    private final MemberFinderService memberFinderService;
    private final ImageRepository repository;

    @Value("${file.path}")
    private String uploadFolder;

    @Transactional
    @Override
    public Void handle(UploadProfileImage cmd) {
        MemberId requesterId = requesterInfo.getMemberId();
        Member member = memberFinderService.find(requesterId);

        try {
            // 파일명 생성
            String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + cmd.image().getOriginalFilename();

            // 실제 파일 저장 경로
            Path imageFilePath = Paths.get(uploadFolder, fileName);
            Files.createDirectories(imageFilePath.getParent());
            Files.write(imageFilePath, cmd.image().getBytes());

            // DB에는 URL 경로 저장
            String imageUrl = "/uploads/images/" + fileName;

            Image image = Image.create(imageUrl);
            repository.save(image);
            member.updateProfileImage(image.getId());

        } catch (IOException e) {
            throw new RuntimeException("프로필 이미지 저장 실패", e);
        }

        return null;
    }
}
