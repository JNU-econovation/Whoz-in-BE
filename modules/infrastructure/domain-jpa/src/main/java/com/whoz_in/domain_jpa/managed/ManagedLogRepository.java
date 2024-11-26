package com.whoz_in.domain_jpa.managed;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManagedLogRepository extends JpaRepository<ManagedLog, String> {

    @Query("SELECT log FROM ManagedLog log WHERE log.logId.ip=:ip ORDER BY log.createdAt DESC LIMIT 1")
    Optional<ManagedLog> findByIp(@Param("ip") String ip);

    @Query("SELECT log FROM ManagedLog log WHERE log.logId.ip=:ip")
    List<ManagedLog> findAllByIp(@Param("ip") String ip);

}
