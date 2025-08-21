package com.whoz_in_infra.infra_jpa.domain.monitor;

import com.whoz_in.domain.network_log.MonitorLog;
import com.whoz_in_infra.infra_jpa.domain.shared.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MonitorLogConverter extends BaseConverter<MonitorLogEntity, MonitorLog> {

    @Override
    public MonitorLogEntity from(MonitorLog log) {
        return new MonitorLogEntity(log.getMac(), log.getRoom());
    }

    @Override
    public MonitorLog to(MonitorLogEntity entity) {
        return new MonitorLog(entity.getMac(), entity.getRoom(), entity.getUpdatedAt());
    }
}
