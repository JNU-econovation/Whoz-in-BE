package com.whoz_in.domain_jpa.badge;

import com.whoz_in.domain_jpa.member.MemberEntity;
import com.whoz_in.domain_jpa.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(BadgeMemberId.class)
public class BadgeMemberEntity extends BaseEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private BadgeEntity badgeEntity;

    public BadgeMemberEntity(MemberEntity memberEntity, BadgeEntity badgeEntity) {
        this.memberEntity = memberEntity;
        this.badgeEntity = badgeEntity;
    }
}
