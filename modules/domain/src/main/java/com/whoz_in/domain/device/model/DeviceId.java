package com.whoz_in.domain.device.model;

import java.util.UUID;

public record DeviceId(UUID id) {
    public DeviceId() {
        this(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
