package com.whoz_in.infra.jpa.shared;

import jakarta.persistence.PreRemove;

public class SoftDeleteListener {
  @PreRemove
  private void preRemove(BaseEntity entity) {
    entity.delete();
  }
}
