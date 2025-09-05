package com.whoz_in_infra.infra_jpa.domain.member;

import com.whoz_in.domain.member.ImageUploadStorage;
import com.whoz_in.domain.member.model.ImageId;
import com.whoz_in.main_api.command.member.exception.FailUploadImageException;
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
public class ImageUploaderStorageImpl implements ImageUploadStorage {

    @Value("${file.path}")
    private String uploadFolder;

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 1024;
    private static final String OUTPUT_FORMAT = "jpg";
    private static final float OUTPUT_QUALITY = 0.5f;

    @Override
    public void save(ImageId imageId, byte[] bytes) {
        try {
            String imgId = imageId.id().toString();
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
