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

    @Value("${file.url-path}")
    private String urlPath;

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 1024;
    private static final String OUTPUT_FORMAT = "jpg";

    @Override
    public void save(ImageId imageId, byte[] bytes) {
        try {
            String imgId = imageId.id().toString();
            Path imageFilePath = Paths.get(uploadFolder, imgId + "." + OUTPUT_FORMAT);
            Files.createDirectories(imageFilePath.getParent());

            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                Thumbnails.of(inputStream)
                        .size(MAX_WIDTH, MAX_HEIGHT)
                        .outputFormat(OUTPUT_FORMAT)
                        .toOutputStream(outputStream);

                Files.write(imageFilePath, outputStream.toByteArray());
            }

        } catch (IOException e) {
            throw FailUploadImageException.EXCEPTION;
        }
    }
}
