package com.whoz_in_infra.infra_jpa.domain.device_connection;

import com.whoz_in_infra.infra_jpa.domain.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "device_connection_entity", indexes = {
        // 특정 일자의 연결들을 찾기 위한 인덱스 TODO: 쿼리의 요구사항인데, 설정 못해서 일단 여기에 둠.. flyway로 관리해야 할듯
        @Index(name = "idx_connected_at", columnList = "connected_at"),
        // device_id 안태워도 충분히 결과가 작으므로 단일 인덱스로 구성함
        @Index(name = "idx_disconnected_at", columnList = "disconnected_at")
})
public class DeviceConnectionEntity extends BaseEntity {
    @Id
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID deviceId;

    @Column(nullable = false)
    private String room;

    @Column(nullable = false)
    private LocalDateTime connectedAt;

    private LocalDateTime disconnectedAt;
}
