package com.whoz_in_infra.infra_jpa.domain.image;

import com.whoz_in.domain.image.model.Image;
import com.whoz_in.domain.image.model.ImageId;
import com.whoz_in_infra.infra_jpa.domain.shared.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class ImageConverter extends BaseConverter<ImageEntity, Image> {

    @Override
    public ImageEntity from(Image image) {
        return new ImageEntity(
                image.getId().id(),
                image.getUrl()
        );
    }

    @Override
    public Image to(ImageEntity entity) {
        return Image.load(
                new ImageId(entity.getId()),
                entity.getImagePath()
        );
    }
}
