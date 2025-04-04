package com.whoz_in.domain_jpa.member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberEntityJpaRepository extends JpaRepository<MemberEntity, UUID> {
    List<MemberEntity> findByName(String name);
    boolean existsBySocialId(String socialId);
    Optional<MemberEntity> findBySocialId(String socialId);
}
