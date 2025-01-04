package com.whoz_in.domain_jpa.managed;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagedLogEntityRepository extends JpaRepository<ManagedLogEntity, String> {

    @Query("SELECT m FROM ManagedLogEntity m WHERE m.logId.ip = :ip ORDER BY m.updatedAt DESC LIMIT 1")
    Optional<ManagedLogEntity> findTopByIpOrderByUpdatedAtDesc(@Param("ip") String ip);

}
