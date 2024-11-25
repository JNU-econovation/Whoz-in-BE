package com.whozin.domain_common_jpa;

import jakarta.persistence.PreRemove;

public class SoftDeleteListener {
  @PreRemove
  private void preRemove(BaseEntity entity) {
    entity.delete();
  }
}
