package com.whoz_in.network_log.domain.managed.repository;

import com.whoz_in.network_log.domain.managed.NetworkLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogJpaRepository extends JpaRepository<NetworkLog, String> {
}
