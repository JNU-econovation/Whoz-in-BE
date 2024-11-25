package com.whozin.common_domain_jpa;

import jakarta.persistence.PreRemove;

public class SoftDeleteListener {
  @PreRemove
  private void preRemove(BaseEntity entity) {
    entity.delete();
  }
}
