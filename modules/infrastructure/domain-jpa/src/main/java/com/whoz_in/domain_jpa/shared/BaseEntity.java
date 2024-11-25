package com.whoz_in.domain_jpa.shared;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, SoftDeleteListener.class})
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder(toBuilder = true)
public abstract class BaseEntity {

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreatedDate
  private Timestamp createdAt;

  @Column(name = "updated_at", nullable = false)
  @LastModifiedDate
  private Timestamp updatedAt;

  @Column(name = "deleted_at")
  private Timestamp deletedAt;

  public void delete() {
    if (this.deletedAt == null) {
      this.deletedAt = Timestamp.valueOf(LocalDateTime.now());
    }
  }

}
