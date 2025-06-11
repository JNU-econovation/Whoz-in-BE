package com.whoz_in_infra.infra_jpa.domain.shared;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

@Getter
@MappedSuperclass
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FilterDef(name = "softDeleteFilter", autoEnabled = true)
@Filter(name = "softDeleteFilter", condition = "deleted_at IS NULL")
public abstract class SoftDeletableEntity extends BaseEntity {
  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;
}
