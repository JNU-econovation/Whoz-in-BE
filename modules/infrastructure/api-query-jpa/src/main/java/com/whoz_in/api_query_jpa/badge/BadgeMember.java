package com.whoz_in.api_query_jpa.badge;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
        + "bm.badge_id AS badge_id, "
        + "bm.is_badge_shown AS is_badge_shown "
        + "FROM badge_member_entity bm")
@Synchronize({"badge_member_entity"})
public class BadgeMember {
    @Id
    private UUID member_id;
    private UUID badge_id;
    private Boolean is_badge_shown;
}
