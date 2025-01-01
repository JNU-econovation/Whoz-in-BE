package com.whoz_in.domain_jpa.member;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberEntityJpaRepository extends JpaRepository<MemberEntity, UUID> {
    Optional<MemberEntity> findByLoginId(String loginId);
    Optional<MemberEntity> findByName(String name);
    Optional<MemberEntity> findById(UUID id);
}
