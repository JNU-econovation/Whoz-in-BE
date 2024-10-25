package com.whoz_in.domain_jpa.shared;

import jakarta.persistence.PreRemove;

public class SoftDeleteListener {
  @PreRemove
  private void preRemove(BaseEntity entity) {
    entity.delete();
  }
}
