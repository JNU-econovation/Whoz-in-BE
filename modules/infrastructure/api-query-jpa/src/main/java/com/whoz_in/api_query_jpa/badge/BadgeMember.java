package com.whoz_in.api_query_jpa.badge;

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
        + "bm.member_id AS member_id, "
        + "b.id AS badge_id, "
        + "b.name AS name, "
        + "b.color_code AS color_code, "
        + "b.badge_type AS badge_type, "
        + "b.created_at AS created_at, "
        + "b.description AS description, "
        + "bm.is_badge_shown AS is_badge_shown "
        + "FROM badge_member_entity bm "
        + "JOIN badge_entity b ON bm.badge_id = b.id")
@Synchronize({"badge_member_entity", "badge_entity"})
public class BadgeMember {
    @Id
    private UUID member_id;
    private UUID badge_id;
    private String name;
    private String color_code;
    private String badge_type;
    private LocalDateTime created_at;
    private String description;
    private Boolean is_badge_shown;
}
