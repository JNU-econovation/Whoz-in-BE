package com.whoz_in.domain_jpa.member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberEntityJpaRepository extends JpaRepository<MemberEntity, UUID> {
    Optional<MemberEntity> findByLoginId(String loginId);
    List<MemberEntity> findByName(String name);
    Optional<MemberEntity> findById(UUID id);
    boolean existsBySocialId(String socialId);
    boolean existsById(UUID id);
    Optional<MemberEntity> findBySocialId(String socialId);
}
