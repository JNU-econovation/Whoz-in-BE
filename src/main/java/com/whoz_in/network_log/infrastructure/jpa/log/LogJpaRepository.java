package com.whoz_in.network_log.infrastructure.jpa.log;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogJpaRepository extends JpaRepository<NetworkLog, String> {
}
