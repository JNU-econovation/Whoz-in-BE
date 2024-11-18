package com.whoz_in.network_log.domain.managed;

import com.whoz_in.network_log.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = ManagedLog.PREFIX)
@SuperBuilder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagedLog extends BaseEntity {

    public static final String PREFIX = "managed_log";

    @EmbeddedId
    private  LogId logId;

    @Column(name = PREFIX + "_wifi_ssid", nullable = true)
    private String wifiSsid;

    @Column(name = PREFIX + "_device_name", nullable = true)
    private String deviceName;

    public static ManagedLog create(LogDTO log) {
        String mac = log.getMac();
        String ip = log.getIp();
        String device = log.getDevice();

        return ManagedLog.builder()
                .logId(new LogId(mac,ip))
                .deviceName(device)
                .build();
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class LogId {

        @Column(name = PREFIX + "_mac", nullable = false)
        private String mac;

        @Column(name = PREFIX + "_ip", nullable = false)
        private String ip;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LogId logId = (LogId) o;
            return logId.mac.equals(mac) && logId.ip.equals(ip);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mac, ip);
        }
    }

}
