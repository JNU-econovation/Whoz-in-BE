package com.whoz_in.domain.image.model;

import java.util.UUID;

public record ImageId(UUID id) {
    public ImageId() {this(UUID.randomUUID());}

    @Override
    public String toString() {return id.toString();}
}
