package com.whoz_in_infra.infra_jpa.domain.member;

import com.whoz_in.domain.member.ImageUploadStorage;
import com.whoz_in.domain.member.model.ImageId;
import com.whoz_in.main_api.command.member.exception.FailUploadImageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageUploaderStorageImpl implements ImageUploadStorage {
    @Value("${file.path}")
    private String uploadFolder;

    @Value("${file.url-path}")
    private String urlPath;

    @Override
    public void save(ImageId imageId, byte[] bytes) {
        try {
            String imgId = imageId.id().toString();
            Path imageFilePath = Paths.get(uploadFolder, imgId);
            Files.createDirectories(imageFilePath.getParent());
            Files.write(imageFilePath, bytes);
        } catch (IOException e) {
            throw FailUploadImageException.EXCEPTION;
        }
    }
}
