package com.whoz_in.domain_jpa.managed;

import com.whoz_in.domain.network_log.ManagedLog;
import com.whoz_in.domain.network_log.ManagedLogRepository;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ManagedLogJpaRepository implements ManagedLogRepository {
    private final ManagedLogEntityRepository repository;
    private final JdbcTemplate jdbcTemplate;
    private final ManagedLogConverter managedLogConverter;

    @Override
    public void save(ManagedLog log) {
        repository.save(managedLogConverter.from(log));
    }

    @Override
    public Optional<ManagedLog> findLatestByRoomAndIp(String room, String ip) {
        return repository.findTopByRoomAndIpOrderByUpdatedAtDesc(room, ip).map(managedLogConverter::to);
    }

    public void saveAll(Collection<ManagedLog> logs) {
        if (logs.isEmpty()) return;

        String sql = "INSERT INTO managed_log_entity " +
                "(mac, created_at, updated_at, device_name, ip, ssid, room) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE updated_at = ?";

        try {
            jdbcTemplate.batchUpdate(sql, logs, logs.size(), (ps, log) -> {
                Timestamp createdAt = Timestamp.valueOf(log.getCreatedAt());
                Timestamp updatedAt = Timestamp.valueOf(log.getUpdatedAt());
                ps.setString(1, log.getMac());
                ps.setTimestamp(2, createdAt);
                ps.setTimestamp(3, updatedAt);
                ps.setString(4, log.getDeviceName());
                ps.setString(5, log.getIp());
                ps.setString(6, log.getSsid());
                ps.setString(7, log.getRoom());
                ps.setTimestamp(7, updatedAt);
            });
        } catch (DuplicateKeyException e) {
            log.error("Duplicate key: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: " + e.getMessage());
        }
    }
}
