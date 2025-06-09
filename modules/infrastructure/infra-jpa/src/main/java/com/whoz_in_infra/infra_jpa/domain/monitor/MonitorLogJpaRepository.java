package com.whoz_in_infra.infra_jpa.domain.monitor;

import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MonitorLogJpaRepository implements MonitorLogRepository {
    private final MonitorLogEntityRepository repository;
    private final JdbcTemplate jdbcTemplate;
    private final MonitorLogConverter converter;

    @Override
    public void save(MonitorLog log) {
        repository.save(converter.from(log));
    }

    @Override
    public void saveAll(Collection<MonitorLog> logs) {
        if (logs.isEmpty()) return;
        //monitor 로그는 발생했는지가 중요하기 때문에 ms가 버려지는 CURRENT_TIMESTAMP를 써도 괜찮음
        String sql = "INSERT INTO monitor_log_entity "
                + "(mac, created_at, updated_at, room) "
                + "VALUES (?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?) "
                + "ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP";
        jdbcTemplate.batchUpdate(sql, logs, logs.size(),
                (ps, log) -> {
                    ps.setString(1, log.getMac());
                    ps.setString(2, log.getRoom());
                });
    }

    @Override
    public boolean existsAfter(String mac, LocalDateTime time) {
        return repository.existsByLogIdMacAndUpdatedAtAfter(mac, time);
    }

    @Override
    public Optional<MonitorLog> findLatestByMacs(Collection<String> macs) {
        return repository.findTopByLogIdMacInOrderByUpdatedAtDesc(macs).map(converter::to);
    }

    @Override
    public Optional<MonitorLog> findLatestByRoomAndMacs(String room, Collection<String> macs) {
        return repository.findTopByLogIdRoomAndLogIdMacInOrderByUpdatedAtDesc(room, macs).map(converter::to);
    }

    @Override
    public List<MonitorLog> findAllByUpdatedAtAfter(LocalDateTime updatedAt) {
        return repository.findAllByUpdatedAtAfter(updatedAt).stream()
                .map(converter::to)
                .toList();
    }
}
