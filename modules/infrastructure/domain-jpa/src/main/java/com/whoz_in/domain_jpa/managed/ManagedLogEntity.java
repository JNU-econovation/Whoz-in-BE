package com.whoz_in.domain_jpa.managed;

import com.whoz_in.domain_jpa.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ManagedLogEntity extends BaseEntity {

    @EmbeddedId
    private LogId logId;

    @Column(name = "ip", nullable = false)
    private String ip;

    @Column(nullable = false)
    private String room;

    private String deviceName;


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    @Embeddable
    public static class LogId {

        @Column(name = "mac", nullable = false)
        private String mac;

        @Column(name = "ssid", nullable = false)
        private String ssid;
    }

}
