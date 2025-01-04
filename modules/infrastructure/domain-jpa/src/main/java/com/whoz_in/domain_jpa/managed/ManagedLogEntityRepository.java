package com.whoz_in.domain_jpa.managed;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagedLogEntityRepository extends JpaRepository<ManagedLogEntity, String> {

    @Query("SELECT m FROM ManagedLogEntity m WHERE m.room = :room AND m.logId.ip = :ip ORDER BY m.updatedAt DESC LIMIT 1")
    Optional<ManagedLogEntity> findTopByRoomAndIpOrderByUpdatedAtDesc(@Param("room") String room, @Param("ip") String ip);

}
