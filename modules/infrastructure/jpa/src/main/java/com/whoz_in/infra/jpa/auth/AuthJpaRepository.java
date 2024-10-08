package com.whoz_in.infra.jpa.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AuthJpaRepository extends JpaRepository<AuthEntity, Long> {

}
