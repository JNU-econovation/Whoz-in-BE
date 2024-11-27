package com.whoz_in.log_writer.monitor;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MonitorLogDAO {

    private final JdbcTemplate jdbcTemplate;

    public void upsertAll(Collection<String> macs) {
        if (macs.isEmpty()) return;

        String sql = "INSERT INTO monitor_log (mac, created_at, updated_at) VALUES (?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP";
        jdbcTemplate.batchUpdate(sql, macs, macs.size(),
                (ps, mac) -> ps.setString(1, mac));
    }


}
