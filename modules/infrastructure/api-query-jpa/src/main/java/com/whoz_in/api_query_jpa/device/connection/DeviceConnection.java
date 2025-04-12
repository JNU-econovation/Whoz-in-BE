package com.whoz_in.api_query_jpa.device.connection;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Immutable
@Table(name = "device_connection_entity")
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
