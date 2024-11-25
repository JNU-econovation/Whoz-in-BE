package com.whoz_in.log_writer.infra.managed;

import com.whoz_in.log_writer.infra.managed.arp.ArpLog;
import com.whoz_in.log_writer.infra.managed.mdns.MdnsLog;
import com.whozin.domain_log_jpa.managed.ManagedLog;
import com.whozin.domain_log_jpa.managed.ManagedLog.LogId;
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
