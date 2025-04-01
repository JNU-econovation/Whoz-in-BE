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

    @Query("SELECT b FROM Badge b, Member m WHERE m.id=:memberId AND b.id=m.mainBadgeId")
    Optional<Badge> findRepresentativeBadge(@Param("memberId") UUID memberId);

    @Query("SELECT b FROM Badge b, Member m, BadgeMember bm WHERE bm.member_id=:memberId AND bm.badge_id=b.id")
    List<Badge> findBadgesByMemberId(@Param("memberId") UUID memberId);
}
