package com.whoz_in.logging;

import java.util.UUID;
import lombok.Getter;

@Getter
public final class TraceId {
    private final String id;

    public TraceId() {
        this.id = createId();
    }

    private String createId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    @Override
    public String toString() {
        return id;
    }
}
