package com.whoz_in.log_writer.managed;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ManagedLogDAO {

    private final JdbcTemplate jdbcTemplate;

    public void insertAll(Collection<ManagedLog> logs) {
        if (logs.isEmpty()) return;
        List<ManagedLog> logList = logs.stream().toList();

        String sql = "INSERT INTO managed_log " +
                "(mac, created_at, updated_at, device_name, ip, ssid) " +
                "values (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE updated_at = ?";

        try{
            batchExecute(sql, logList);
        } catch(DuplicateKeyException e){
            System.err.println("[ERROR] Duplicate key: "+ e.getMessage());
        } catch (Exception e){
            System.err.println("[ERROR] Unexpected error: "+ e.getMessage());
        }

    }

    private void batchExecute(String sql, List<ManagedLog> logList){
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                ManagedLog networkLog = logList.get(i);
                //같은 기기더라도 맥, 아이피가 다른 로그들이 발생 가능.
                //따라서 마지막에 발생한 로그를 알아내기 위해 ms는 버려지는 CURRENT_TIMESTAMP 대신 로그에서 발생 시각 추출
                String logTime = Timestamp.valueOf(networkLog.getCreatedAt()).toString();
                preparedStatement.setString(1, networkLog.getMac());
                preparedStatement.setString(2, logTime);
                preparedStatement.setString(3, logTime);
                preparedStatement.setString(4, networkLog.getDeviceName());
                preparedStatement.setString(5, networkLog.getIp());
                preparedStatement.setString(6, networkLog.getSsid());
                preparedStatement.setString(7, logTime);

            }

            @Override
            public int getBatchSize() {
                return logList.size();
            }
        });
    }

}
