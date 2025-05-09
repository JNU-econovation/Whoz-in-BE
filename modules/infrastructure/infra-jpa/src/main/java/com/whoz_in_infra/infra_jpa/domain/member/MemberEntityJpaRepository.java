package com.whoz_in_infra.infra_jpa.domain.member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberEntityJpaRepository extends JpaRepository<MemberEntity, UUID> {
    Optional<MemberEntity> findOneById(UUID memberId);
    List<MemberEntity> findByName(String name);
    boolean existsBySocialId(String socialId);
    Optional<MemberEntity> findBySocialId(String socialId);
}
