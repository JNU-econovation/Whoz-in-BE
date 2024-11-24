package com.whoz_in.network_log.infra.managed.mdns;

import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

//TODO: mac과 ip를 equals and hashcode
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class MdnsLog {
    @EqualsAndHashCode.Include
    private final String mac;
    @EqualsAndHashCode.Include
    private final String ip;

    private final String device;
    private final LocalDateTime createdAt;

    //기기 이름이 없는 경우 null
    public MdnsLog(String mac, String ip, @Nullable String device) {
        this.mac = mac;
        this.ip = ip;
        this.device = device;
        this.createdAt = LocalDateTime.now();
    }
}
