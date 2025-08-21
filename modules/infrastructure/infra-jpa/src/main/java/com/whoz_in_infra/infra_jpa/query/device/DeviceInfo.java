package com.whoz_in_infra.infra_jpa.query.device;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Getter
@Table(name = "device_info_entity")
@Immutable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceInfo {
    @Id
    private Long id;

    @Column(name ="device_id", nullable = false) //TODO: 자동 스네이크 케이스 적용
    private UUID deviceId;

    private String mac;

    private String ssid;
}
