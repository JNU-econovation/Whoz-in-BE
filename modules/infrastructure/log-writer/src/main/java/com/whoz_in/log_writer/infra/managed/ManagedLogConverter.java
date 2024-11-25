package com.whoz_in.log_writer.infra.managed;

import com.whoz_in.domain_jpa.log.managed.ManagedLog;
import com.whoz_in.domain_jpa.log.managed.ManagedLog.LogId;
import com.whoz_in.log_writer.infra.managed.arp.ArpLog;
import com.whoz_in.log_writer.infra.managed.mdns.MdnsLog;
import java.sql.Timestamp;

public class ManagedLogConverter {

    public static ManagedLog toEntity(MdnsLog log){
        return ManagedLog.builder()
                .logId(new LogId(log.getMac(), log.getIp()))
                .deviceName(log.getDevice())
                .createdAt(Timestamp.valueOf(log.getCreatedAt()))
                .build();
    }

    public static ManagedLog toEntity(ArpLog log){
        return ManagedLog.builder()
                .logId(new LogId(log.getMac(), log.getIp()))
                .deviceName(log.getDevice())
                .createdAt(Timestamp.valueOf(log.getCreatedAt()))
                .build();
    }

}
