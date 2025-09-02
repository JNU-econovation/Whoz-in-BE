package com.whoz_in_infra.infra_jpa.domain.image;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageEntityJpaRepository extends JpaRepository<ImageEntity, UUID> {
}
