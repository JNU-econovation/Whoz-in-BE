package com.whoz_in.domain_jpa.monitor;

import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import java.security.MessageDigest;
import java.util.Collection;
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
                + "(mac, created_at, updated_at) "
                + "VALUES (?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) "
                + "ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP";
        jdbcTemplate.batchUpdate(sql, logs, logs.size(),
                (ps, log) -> ps.setString(1, log.getMac()));
    }
}
