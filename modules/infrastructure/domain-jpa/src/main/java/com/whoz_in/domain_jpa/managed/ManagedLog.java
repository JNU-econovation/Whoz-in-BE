package com.whoz_in.domain_jpa.managed;

import com.whoz_in.domain_jpa.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = ManagedLog.TABLE_NAME)
@SuperBuilder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagedLog extends BaseEntity {

    public static final String TABLE_NAME = "managed_log";

    @EmbeddedId
    private LogId logId;

    @Column(name = TABLE_NAME + "_wifi_ssid", nullable = true)
    private String wifiSsid;

    @Column(name = TABLE_NAME + "_device_name", nullable = true)
    private String deviceName;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class LogId {

        @Column(name = TABLE_NAME + "_mac", nullable = false)
        private String mac;

        @Column(name = TABLE_NAME + "_ip", nullable = false)
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
