package com.whoz_in.network_log.domain.managed.repository;

import com.whoz_in.network_log.domain.managed.ManagedLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LogJpaRepository extends JpaRepository<ManagedLog, String> {

    @Query("SELECT log FROM ManagedLog log WHERE log.logId.ip=:ip")
    Optional<ManagedLog> findByIp(@Param("ip") String ip);

}
