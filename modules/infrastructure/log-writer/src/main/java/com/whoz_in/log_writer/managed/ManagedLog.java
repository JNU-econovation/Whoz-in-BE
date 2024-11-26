package com.whoz_in.log_writer.managed;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ManagedLog {
    @EqualsAndHashCode.Include
    private final String mac;
    @EqualsAndHashCode.Include
    private final String ip;
    private final String deviceName;
    @Setter //Parser에선 로그로부터 ssid를 얻을 수 없기 때문에 추후에 설정할 수 있도록 함
    private String ssid;
    private final LocalDateTime createdAt;

    public ManagedLog(String mac, String ip, String deviceName) {
        this.mac = mac;
        this.ip = ip;
        this.deviceName = deviceName;
        this.createdAt = LocalDateTime.now();
    }
}
