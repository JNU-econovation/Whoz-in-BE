package com.whoz_in.api_query_jpa.badge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Entity
@Immutable
@Subselect("SELECT "
        + "b.id, "
        + "b.name, "
        + "b.color_code, "
        + "b.badge_type, "
        + "b.created_at, "
        + "b.description "
        + "FROM badge_entity b")
@Synchronize({"badge_entity"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Badge {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "badge_type")
    private String badgeType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "description")
    private String description;
}
