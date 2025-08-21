package com.whoz_in_infra.infra_jpa.query.device.connection;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Immutable
@Table(name = "device_connection_entity")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceConnection {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "device_id")
    private UUID deviceId;

    @Column(name = "room")
    private String room;

    @Column(name = "connected_at")
    private LocalDateTime connectedAt;

    @Column(name = "disconnected_at")
    private LocalDateTime disconnectedAt;
}
