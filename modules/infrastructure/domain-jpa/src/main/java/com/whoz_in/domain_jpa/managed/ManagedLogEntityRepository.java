package com.whoz_in.domain_jpa.managed;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagedLogEntityRepository extends JpaRepository<ManagedLogEntity, String> {

    @Query("SELECT m FROM ManagedLogEntity m WHERE m.logId.ip = :ip AND m.updatedAt > :time ORDER BY m.updatedAt DESC LIMIT 1")
    Optional<ManagedLogEntity> findTopByIpOrderByUpdatedAtDescAfter(@Param("ip") String ip, @Param("time") LocalDateTime time);

    @Query(value = "SELECT * FROM whozin.managed_log_entity m WHERE m.ssid = :ssid AND m.mac = :mac LIMIT 1", nativeQuery = true)
    Optional<ManagedLogEntity> findBySsidAndMac(@Param("ssid") String ssid, @Param("mac") String mac);
}
