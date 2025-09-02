package com.whoz_in.domain.image.model;

import com.whoz_in.domain.shared.AggregateRoot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access =  AccessLevel.PRIVATE)
public class Image extends AggregateRoot {
    private final ImageId id;
    private String url;

    public static Image create(String url) {
        Image image = builder()
                .id(new ImageId())
                .url(url)
                .build();
        return image;
    }

    public static Image load(ImageId id, String url) {
        return builder()
                .id(id)
                .url(url)
                .build();
    }
}
