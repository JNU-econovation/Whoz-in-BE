package com.whoz_in.network_log.infra.managed;

import com.whoz_in.domain_jpa.log.managed.ManagedLog;
import com.whoz_in.domain_jpa.log.managed.ManagedLog.LogId;
import com.whoz_in.network_log.infra.managed.arp.ArpLog;
import com.whoz_in.network_log.infra.managed.mdns.MdnsLog;

public class ManagedLogConverter {

    public static ManagedLog toEntity(MdnsLog log){
        return ManagedLog.builder()
                .logId(new LogId(log.getMac(), log.getIp()))
                .deviceName(log.getDevice())
                .createdAt(log.getCreatedAt())
                .build();
    }

    public static ManagedLog toEntity(ArpLog log){
        return ManagedLog.builder()
                .logId(new LogId(log.getMac(), log.getIp()))
                .deviceName(log.getDevice())
                .createdAt(log.getCreatedAt())
                .build();
    }

}
