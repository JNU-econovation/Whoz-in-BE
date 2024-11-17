package com.whoz_in.networklog.infrastructure.jpa.log;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogJpaRepository extends JpaRepository<NetworkLog, String> {
}
