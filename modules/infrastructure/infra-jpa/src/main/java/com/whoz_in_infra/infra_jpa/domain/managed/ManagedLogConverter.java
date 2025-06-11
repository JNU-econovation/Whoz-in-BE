package com.whoz_in_infra.infra_jpa.domain.managed;

import com.whoz_in.domain.network_log.ManagedLog;
import com.whoz_in_infra.infra_jpa.domain.shared.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class ManagedLogConverter extends BaseConverter<ManagedLogEntity, ManagedLog> {

    @Override
    public ManagedLogEntity from(ManagedLog log) {
        return new ManagedLogEntity(
                new ManagedLogEntity.LogId(log.getMac(), log.getSsid()),
                log.getIp(), log.getRoom(), log.getDeviceName());
    }

    @Override
    public ManagedLog to(ManagedLogEntity entity) {
        return new ManagedLog(
                entity.getLogId().getMac(), entity.getIp(),
                entity.getDeviceName(), entity.getLogId().getSsid(), entity.getRoom(),
                entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
