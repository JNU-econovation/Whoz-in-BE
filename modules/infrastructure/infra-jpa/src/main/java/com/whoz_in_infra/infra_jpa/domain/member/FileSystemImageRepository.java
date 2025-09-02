package com.whoz_in_infra.infra_jpa.domain.member;

import com.whoz_in.domain.member.ImageRepository;
import com.whoz_in.main_api.command.member.exception.FailUploadImageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileSystemImageRepository implements ImageRepository {
    @Value("${file.path}")
    private String uploadFolder;

    @Value("${file.url-path}")
    private String urlPath;

    @Override
    public String save(byte[] bytes) {
        try {
            String fileName = UUID.randomUUID().toString();
            Path imageFilePath = Paths.get(uploadFolder, fileName);
            Files.createDirectories(imageFilePath.getParent());
            Files.write(imageFilePath, bytes);
            String imageUrl = urlPath + fileName;
            return imageUrl;
        } catch (IOException e) {
            throw FailUploadImageException.EXCEPTION;
        }
    }
}
