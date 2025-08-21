package com.whoz_in_infra.infra_jpa.domain.managed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagedLogEntityRepository extends JpaRepository<ManagedLogEntity, String> {

    @Query(value = "SELECT * " +
            "FROM managed_log_entity " +
            "WHERE mac = (" +
            "    SELECT mac " +
            "    FROM managed_log_entity " +
            "    WHERE ip = :ip " +
            "    ORDER BY updated_at DESC" +
            "    LIMIT 1)"
            + " AND ip = :ip AND updated_at >= :time",
            nativeQuery = true)
    List<ManagedLogEntity> findAllByIpLatestMac(@Param("ip") String ip, @Param("time") LocalDateTime time);

    @Query(value = "SELECT * FROM whozin.managed_log_entity m WHERE m.ssid = :ssid AND m.mac = :mac LIMIT 1", nativeQuery = true)
    Optional<ManagedLogEntity> findBySsidAndMac(@Param("ssid") String ssid, @Param("mac") String mac);
}
