package com.whoz_in.network_api.managed;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class ParsedLog {
    @EqualsAndHashCode.Include
    private final String mac;
    @EqualsAndHashCode.Include
    private final String ip;
    private final String deviceName;
    //같은 기기더라도 맥, 아이피가 다른 로그들이 발생 가능.
    //따라서 마지막에 발생한 로그를 알아내기 위해 ms는 버려지는 CURRENT_TIMESTAMP 대신 로그에서 발생 시각 추출
    private final LocalDateTime createdAt = LocalDateTime.now();

    public ParsedLog(String mac, String ip, String deviceName) {
        this.mac = mac;
        this.ip = ip;
        this.deviceName = deviceName;
    }
}
