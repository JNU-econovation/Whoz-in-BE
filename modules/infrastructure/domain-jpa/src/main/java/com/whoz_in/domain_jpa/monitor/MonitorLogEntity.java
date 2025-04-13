package com.whoz_in.domain_jpa.monitor;


import com.whoz_in.domain_jpa.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
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
@Table(name = "monitor_log_entity", indexes = {
        /*
            updated_at을 통해 최근 로그를 조회할 일이 많으므로 추가함
            updated_at으로 내림차순 정렬이나 limit이 필요한 경우 DESC 인덱스 고려
         */
        @Index(name = "idx_updated_at", columnList = "updated_at")
})
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
