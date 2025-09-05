package com.whoz_in.domain.member.model;

import java.util.UUID;

public record ProfileImageId(UUID id) {
    public ProfileImageId() {
        this(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
