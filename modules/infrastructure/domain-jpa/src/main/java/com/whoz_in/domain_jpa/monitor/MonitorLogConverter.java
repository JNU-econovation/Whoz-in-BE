package com.whoz_in.domain_jpa.monitor;

import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in.domain_jpa.shared.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MonitorLogConverter extends BaseConverter<MonitorLogEntity, MonitorLog> {

    @Override
    public MonitorLogEntity from(MonitorLog log) {
        return new MonitorLogEntity(log.getMac(), log.getRoom());
    }

    @Override
    public MonitorLog to(MonitorLogEntity entity) {
        return new MonitorLog(entity.getMac(), entity.getRoom(), entity.getCreatedAt());
    }
}
