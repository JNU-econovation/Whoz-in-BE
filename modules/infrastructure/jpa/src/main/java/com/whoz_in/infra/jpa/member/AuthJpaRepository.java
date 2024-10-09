package com.whoz_in.infra.jpa.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthJpaRepository extends JpaRepository<AuthEntity, Long> {

}
