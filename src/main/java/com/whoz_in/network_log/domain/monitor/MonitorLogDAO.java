package com.whoz_in.network_log.domain.monitor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MonitorLogRepository{
    @PersistenceContext
    private EntityManager em;

    @Modifying
    @Transactional
    void upsertAll(Set<String> macs) {
        if (macs.isEmpty()) return;
        // 동적 SQL 생성
        StringBuilder sql = new StringBuilder("INSERT INTO monitor_log (mac, created_date, updated_date) VALUES ");
        macs.forEach(mac -> sql.append(String.format("('%s', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),", mac)));
        sql.setLength(sql.length() - 1); // 마지막 쉼표 제거
        sql.append(" ON DUPLICATE KEY UPDATE updated_date = CURRENT_TIMESTAMP");
//        System.out.println(sql);
        // 동적 SQL 실행
        em.createNativeQuery(sql.toString()).executeUpdate();
    }
}
