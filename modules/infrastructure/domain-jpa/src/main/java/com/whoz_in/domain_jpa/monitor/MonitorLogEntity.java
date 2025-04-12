package com.whoz_in.domain_jpa.monitor;


import com.whoz_in.domain_jpa.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonitorLogEntity extends BaseEntity {
    @EmbeddedId
    private LogId logId;

    public String getMac() {
        return logId.getMac();
    }

    public String getRoom() {
        return logId.getRoom();
    }

    public MonitorLogEntity(String mac, String room) {
        this.logId = new LogId(mac, room);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode
    @Embeddable
    public static class LogId implements Serializable {

        @Column(nullable = false)
        private String mac;

        @Column(nullable = false)
        private String room;
    }
}
