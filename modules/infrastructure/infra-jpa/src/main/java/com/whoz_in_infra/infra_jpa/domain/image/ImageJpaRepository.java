package com.whoz_in_infra.infra_jpa.domain.image;

import com.whoz_in.domain.image.ImageRepository;
import com.whoz_in.domain.image.model.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageJpaRepository implements ImageRepository {
    private final ImageConverter imageConverter;
    private final ImageEntityJpaRepository imageRepo;

    @Override
    public void save(Image image) {
        ImageEntity imageEntity = imageConverter.from(image);
        imageRepo.save(imageEntity);
    }
}
