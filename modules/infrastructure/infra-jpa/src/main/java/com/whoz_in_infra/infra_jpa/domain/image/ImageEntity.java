package com.whoz_in_infra.infra_jpa.domain.image;

import com.whoz_in_infra.infra_jpa.domain.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageEntity extends BaseEntity {
    @Id
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    private String imagePath;

    public ImageEntity(UUID id, String url) {
        this.id = id;
        this.imagePath = url;
    }
}
