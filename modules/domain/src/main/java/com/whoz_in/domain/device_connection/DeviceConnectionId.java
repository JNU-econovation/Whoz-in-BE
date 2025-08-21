package com.whoz_in.domain.device_connection;

import java.util.UUID;

public record DeviceConnectionId(UUID id) {
    public DeviceConnectionId() {
        this(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
