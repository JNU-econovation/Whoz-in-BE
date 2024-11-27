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

        //monitor 로그는 발생했는지가 중요하기 때문에 ms가 버려지는 CURRENT_TIMESTAMP를 써도 괜찮음
        String sql = "INSERT INTO monitor_log (mac, created_at, updated_at) VALUES (?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP";
        jdbcTemplate.batchUpdate(sql, macs, macs.size(),
                (ps, mac) -> ps.setString(1, mac));
    }


}
