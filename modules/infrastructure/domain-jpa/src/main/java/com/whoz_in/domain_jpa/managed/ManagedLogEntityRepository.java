package com.whoz_in.domain_jpa.managed;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagedLogEntityRepository extends JpaRepository<ManagedLogEntity, String> {

    @Query("SELECT log FROM ManagedLogEntity log WHERE log.logId.ip=:ip ORDER BY log.createdAt DESC LIMIT 1")
    Optional<ManagedLogEntity> findByIp(@Param("ip") String ip);

    @Query("SELECT log FROM ManagedLogEntity log WHERE log.logId.ip=:ip")
    List<ManagedLogEntity> findAllByIp(@Param("ip") String ip);

}
