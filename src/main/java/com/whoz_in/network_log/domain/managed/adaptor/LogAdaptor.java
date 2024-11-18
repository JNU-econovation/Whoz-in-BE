package com.whoz_in.network_log.domain.managed.adaptor;

import com.whoz_in.network_log.domain.managed.repository.LogRepository;
import com.whoz_in.network_log.infrastructure.jpa.log.LogJpaRepository;
import com.whoz_in.network_log.infrastructure.jpa.log.NetworkLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(Collection<NetworkLog> logs) {
        logJpaRepository.saveAll(logs);
    }

    @Override
    public void bulkInsert(Collection<NetworkLog> logs) {
        // TODO: Bulk Insert 구현

        List<NetworkLog> logList = logs.stream().toList();

        String sql = new StringBuilder()
                .append("INSERT INTO network_log ")
                .append("(managed_log_mac_address, create_date, update_date,managed_log_device_name, managed_log_ip_address, managed_log_wifi_ssid)")
                .append("values (?, now(), now(), ?, ?, ?)")
                .toString();

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) {
                NetworkLog networkLog = logList.get(i);

                try {
                    preparedStatement.setString(1, networkLog.getMacAddress());
                    preparedStatement.setString(4, networkLog.getDeviceName());
                    preparedStatement.setString(5, networkLog.getIpAddress());
                    preparedStatement.setString(6, networkLog.getWifiSsid());
                    preparedStatement.execute();
                } catch (SQLException e) {
                    System.err.println("[ERROR] SQL Exception: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            @Override
            public int getBatchSize() {
                return 100;
            }
        });

    }

    @Override
    public void save(NetworkLog log) {
        logJpaRepository.save(log);
    }
}
