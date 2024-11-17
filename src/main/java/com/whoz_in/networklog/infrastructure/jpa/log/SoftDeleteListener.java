package com.whoz_in.networklog.infrastructure.jpa.log;

import jakarta.persistence.PreRemove;

public class SoftDeleteListener {

    @PreRemove
    private void preRemove(BaseEntity entity) {
        entity.delete();
    }
}
