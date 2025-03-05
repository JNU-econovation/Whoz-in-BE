package com.whoz_in.domain_jpa.managed;

import com.whoz_in.domain.network_log.ManagedLog;
import com.whoz_in.domain.network_log.ManagedLogRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    public void saveAll(Collection<ManagedLog> logs) {
        if (logs.isEmpty()) return;

        String sql = "INSERT INTO managed_log_entity " +
                "(mac, created_at, updated_at, device_name, ip, ssid, room) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE updated_at = ?, ssid = ?";

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
                ps.setTimestamp(8, updatedAt);
                ps.setString(9, log.getSsid());
            });
        } catch (DuplicateKeyException e) {
            log.error("Duplicate key: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: " + e.getMessage());
        }
    }

    public Optional<ManagedLog> findLatestByIpAfter(String ip, LocalDateTime time) {
        return repository.findTopByIpOrderByUpdatedAtDescAfter(ip, time).map(managedLogConverter::to);
    }

    @Override
    public Optional<ManagedLog> findBySsidAndMac(String ssid, String mac) {
        return repository.findBySsidAndMac(ssid, mac).map(managedLogConverter::to);
    }
}
