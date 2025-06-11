package com.whoz_in_infra.infra_jpa.domain.badge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeMemberEntityRepository extends JpaRepository<BadgeMemberEntity, Long> {
}
