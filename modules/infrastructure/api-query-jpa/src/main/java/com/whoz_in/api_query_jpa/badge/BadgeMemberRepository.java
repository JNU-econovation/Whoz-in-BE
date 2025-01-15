package com.whoz_in.api_query_jpa.badge;

import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeMemberRepository extends JpaRepository<BadgeMember, Long> {
    @Query("SELECT bm.badgeId FROM BadgeMember bm")
    Set<UUID> findByMemberId(UUID memberId);
}
