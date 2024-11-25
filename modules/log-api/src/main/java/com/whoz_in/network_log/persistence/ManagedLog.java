package com.whoz_in.network_log.persistence;

import com.whoz_in.network_log.infra.managed.arp.ArpLog;
import com.whoz_in.network_log.infra.managed.mdns.MdnsLog;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    public static ManagedLog create(MdnsLog log) {
        String mac = log.getMac();
        String ip = log.getIp();
        String device = log.getDevice();

        return ManagedLog.builder()
                .logId(new LogId(mac,ip))
                .deviceName(device)
                .build();
    }

    public static ManagedLog create(ArpLog log){
        String mac = log.getMac();
        String ip = log.getIp();
        String device = log.getDevice();
        String ssid = log.getSsid();
        LocalDateTime createdDate = log.getCreatedDate();

        return ManagedLog.builder()
                .logId(new LogId(mac, ip))
                .deviceName(device)
                .wifiSsid(ssid)
                .createdDate(Timestamp.valueOf(createdDate))
                .build();
    }

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
