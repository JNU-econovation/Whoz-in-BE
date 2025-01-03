package com.whoz_in.domain.network_log;


import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public final class MonitorLog {
    private final String mac;
    private final String room;
    private final LocalDateTime updatedAt;

    public MonitorLog(String mac, String room) {
        this.mac = mac;
        this.room = room;
        this.updatedAt = null; //Repository 구현체에서 처리
    }

    public MonitorLog(String mac, String room, LocalDateTime updatedAt) {
        this.mac = mac;
        this.room = room;
        this.updatedAt = updatedAt;
    }
}
