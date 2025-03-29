package com.whoz_in.api_query_jpa.badge;

import com.whoz_in.main_api.query.badge.application.view.BadgesOfMember;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, UUID> {
    Optional<Badge> findById(UUID id);
    @Query("SELECT b FROM Badge b WHERE b.created_at <= :threshold AND b.badge_type = 'USERMADE'")
    List<Badge> findAllActivatedBadges(@Param("threshold") LocalDateTime threshold);

    @Query("SELECT BadgesOfMember.BadgeOfMember( " +
            "bm.badge_id, bm.name, bm.color_code, bm.description, bm.is_badge_shown) " +
            "FROM BadgeMember bm WHERE bm.member_id = :memberId")
    List<BadgesOfMember.BadgeOfMember> findBadgesByMemberId(@Param("memberId") UUID memberId);
}
