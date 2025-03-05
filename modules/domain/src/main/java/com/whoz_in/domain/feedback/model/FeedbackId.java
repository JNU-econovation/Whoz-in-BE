package com.whoz_in.domain.feedback.model;

import java.util.UUID;

public record FeedbackId(UUID id) {
    public FeedbackId() {this(UUID.randomUUID());};

    @Override
    public String toString() {return id.toString();}
}
