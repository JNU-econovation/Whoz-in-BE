package com.whoz_in.api_query_jpa.badge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Getter
@Entity
@Immutable
@Subselect("SELECT "
        + "bm.member_id, "
        + "bm.badge_id, "
        + "b.id, "
        + "b.name, "
        + "b.color_code, "
        + "b.badge_type, "
        + "b.created_at, "
        + "b.description, "
        + "bm.is_badge_shown "
        + "FROM badge_member_entity bm "
        + "JOIN badge_entity b ON bm.badge_id = b.id")
@Synchronize({"badge_member_entity", "badge_entity"})
public class BadgeMember {
    @Id
    @Column(name = "member_id")
    private UUID memberId;

    @Column(name = "badge_id")
    private UUID badgeId;

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

    @Column(name = "is_badge_shown")
    private Boolean isBadgeShown;
}
