package com.whoz_in_infra.infra_jpa.domain.badge;

import com.whoz_in_infra.infra_jpa.domain.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(BadgeMemberId.class)
public class BadgeMemberEntity extends BaseEntity {
    @Id
    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @Id
    @Column(name = "badge_id", nullable = false)
    private UUID badgeId;

    @Column(nullable = false)
    private Boolean isBadgeShown = true;

    public BadgeMemberEntity(UUID memberId, UUID badgeId, Boolean isBadgeShown) {
        this.memberId = memberId;
        this.badgeId = badgeId;
        this.isBadgeShown = isBadgeShown;
    }
}
