package com.whoz_in.main_api.shared.persistence;

import com.whoz_in.domain.member.model.ProfileImageId;
import com.whoz_in.main_api.command.member.exception.FailUploadImageException;
import com.whoz_in.main_api.shared.utils.ImageUploadStorage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileSystemImageUploadStorage implements ImageUploadStorage {
    @Value("${file.path}")
    private String uploadFolder;

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 1024;
    private static final String OUTPUT_FORMAT = "jpg";
    private static final float OUTPUT_QUALITY = 0.5f;

    @Override
    public void save(ProfileImageId profileImageId, byte[] bytes) {
        try {
            String imgId = profileImageId.id().toString();
            Path imageFilePath = Paths.get(uploadFolder, imgId + "." + OUTPUT_FORMAT);
            Files.createDirectories(imageFilePath.getParent());

            byte[] optimizedImage = optimizeImage(bytes);

            Files.write(imageFilePath, optimizedImage);

        } catch (IOException e) {
            throw FailUploadImageException.EXCEPTION;
        }
    }

    private byte[] optimizeImage(byte[] originalBytes) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(originalBytes);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Thumbnails.of(inputStream)
                    .size(MAX_WIDTH, MAX_HEIGHT)
                    .outputFormat(OUTPUT_FORMAT)
                    .outputQuality(OUTPUT_QUALITY)
                    .toOutputStream(outputStream);

            return outputStream.toByteArray();
        }
    }
}
