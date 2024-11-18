package com.whoz_in.network_log.domain.managed.adaptor;

import com.whoz_in.network_log.domain.managed.repository.LogRepository;
import com.whoz_in.network_log.domain.managed.repository.LogJpaRepository;
import com.whoz_in.network_log.domain.managed.ManagedLog;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogAdaptor implements LogRepository {
    
    private final LogJpaRepository logJpaRepository;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(Collection<ManagedLog> logs) {
        logJpaRepository.saveAll(logs);
    }

    @Override
    public void bulkInsert(Collection<ManagedLog> logs) {
        // TODO: Bulk Insert 구현
        if(logs.size() > 0) {
            List<ManagedLog> logList = logs.stream().toList();

            String sql = new StringBuilder()
                    .append("INSERT INTO network_log ")
                    .append("(managed_log_mac_address, create_date, update_date,managed_log_device_name, managed_log_ip_address, managed_log_wifi_ssid)")
                    .append("values (?, now(), now(), ?, ?, ?)")
                    .toString();

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) {
                    try {
                        ManagedLog networkLog = logList.get(i);
                        preparedStatement.setString(1, networkLog.getLogId().getMac());
                        preparedStatement.setString(4, networkLog.getDeviceName());
                        preparedStatement.setString(5, networkLog.getLogId().getIp());
                        preparedStatement.setString(6, networkLog.getWifiSsid());
                        preparedStatement.execute();
                    } catch (SQLException e) {
                        System.err.println("[ERROR] SQL Exception: " + e.getMessage());
                        throw new RuntimeException(e);
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }

                @Override
                public int getBatchSize() {
                    return 100;
                }
            });
        }

    }

    @Override
    public void save(ManagedLog log) {
        logJpaRepository.save(log);
    }
}
