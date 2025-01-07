package com.whoz_in.domain_jpa.badge;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeEntityRepository extends JpaRepository<BadgeEntity, Long> {
    Optional<BadgeEntity> findByName(String name);
//    @Query(value = "SELECT * FROM badge_entity WHERE name = :name", nativeQuery = true)
//    Optional<BadgeEntity> findByName(@Param("name") String name);
}
