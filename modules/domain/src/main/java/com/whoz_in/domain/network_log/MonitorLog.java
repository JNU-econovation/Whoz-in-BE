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

    //MonitorLog의 updateAt은 정확하지 않아도 되기 때문에 구현체에서 일괄로 처리합니다.
    //따라서 이 생성자는 읽을 때만 쓰도록 합시다.
    public MonitorLog(String mac, String room, LocalDateTime updatedAt) {
        this.mac = mac;
        this.room = room;
        this.updatedAt = updatedAt;
    }
}
