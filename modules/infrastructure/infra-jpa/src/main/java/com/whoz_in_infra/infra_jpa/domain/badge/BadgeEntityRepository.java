package com.whoz_in_infra.infra_jpa.domain.badge;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeEntityRepository extends JpaRepository<BadgeEntity, Long> {
    Optional<BadgeEntity> findByName(String name);
    Optional<BadgeEntity> findOneById(UUID id);
}
