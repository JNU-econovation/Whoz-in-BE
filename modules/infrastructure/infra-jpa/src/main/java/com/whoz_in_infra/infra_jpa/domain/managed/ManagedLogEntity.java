package com.whoz_in_infra.infra_jpa.domain.managed;

import com.whoz_in_infra.infra_jpa.domain.shared.BaseEntity;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ManagedLogEntity extends BaseEntity {

    @EmbeddedId
    private LogId logId;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private String room;

    private String deviceName;


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode
    @Embeddable
    public static class LogId implements Serializable {

        @Column(nullable = false)
        private String mac;

        @Column(nullable = false)
        private String ssid;
    }

}
