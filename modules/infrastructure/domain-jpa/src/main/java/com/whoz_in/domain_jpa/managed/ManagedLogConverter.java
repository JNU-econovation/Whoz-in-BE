package com.whoz_in.domain_jpa.managed;

import com.whoz_in.domain.network_log.ManagedLog;
import com.whoz_in.domain_jpa.shared.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class ManagedLogConverter extends BaseConverter<ManagedLogEntity, ManagedLog> {

    @Override
    public ManagedLogEntity from(ManagedLog log) {
        return new ManagedLogEntity(
                new ManagedLogEntity.LogId(log.getMac(), log.getIp()),
                log.getSsid(), log.getDeviceName());
    }

    @Override
    public ManagedLog to(ManagedLogEntity entity) {
        return new ManagedLog(entity.getDeviceName(),
                entity.getLogId().getIp(), entity.getLogId().getMac(),
                entity.getDeviceName(), entity.getCreatedAt());
    }
}
