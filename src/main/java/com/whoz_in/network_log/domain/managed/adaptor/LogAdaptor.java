package com.whoz_in.network_log.domain.managed.adaptor;

import com.whoz_in.network_log.domain.managed.repository.LogRepository;
import com.whoz_in.network_log.domain.managed.repository.LogJpaRepository;
import com.whoz_in.network_log.domain.managed.ManagedLog;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
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
                    .append("INSERT INTO " + ManagedLog.TABLE_NAME)
                    .append("(managed_log_mac, created_date, updated_date, managed_log_device_name, managed_log_ip, managed_log_wifi_ssid)")
                    .append("values (?, now(), now(), ?, ?, ?)")
                    .toString();

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) {
                    try {
                        ManagedLog networkLog = logList.get(i);
                        preparedStatement.setString(1, networkLog.getLogId().getMac());
                        preparedStatement.setString(2, networkLog.getDeviceName());
                        preparedStatement.setString(3, networkLog.getLogId().getIp());
                        preparedStatement.setString(4, networkLog.getWifiSsid());
                        preparedStatement.execute();
                    } catch (SQLIntegrityConstraintViolationException e){
                        System.err.println("[ERROR] SQL Exception: " + e.getMessage());
                    } catch(DuplicateKeyException e){
                        System.err.println("[ERROR] Duplicate key: " + e.getMessage());
                    } catch (SQLException e) {
                        System.err.println("[ERROR] SQL Exception: " + e.getMessage());
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
