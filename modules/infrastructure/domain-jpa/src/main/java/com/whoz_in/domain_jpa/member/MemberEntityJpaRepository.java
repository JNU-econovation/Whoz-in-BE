package com.whoz_in.domain_jpa.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberEntityJpaRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByLoginId(String loginId);
    Optional<MemberEntity> findByName(String name);
}
