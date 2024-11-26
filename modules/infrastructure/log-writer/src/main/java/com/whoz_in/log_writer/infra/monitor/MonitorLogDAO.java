package com.whoz_in.log_writer.infra.monitor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MonitorLogDAO {
    @PersistenceContext
    private EntityManager em;

    @Modifying
    @Transactional
    public void upsertAll(Set<String> macs) {
        if (macs.isEmpty()) return;
        StringBuilder sql = new StringBuilder("INSERT INTO monitor_log (mac, created_date, updated_date) VALUES ");
        macs.forEach(mac -> sql.append(String.format("('%s', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),", mac)));
        sql.setLength(sql.length() - 1);
        sql.append(" ON DUPLICATE KEY UPDATE updated_date = CURRENT_TIMESTAMP");
        em.createNativeQuery(sql.toString()).executeUpdate();
    }
}
