package com.whoz_in.api_query_jpa.badge;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeMemberRepository extends JpaRepository<BadgeMember, Long> {
    @Query("SELECT bm FROM BadgeMember bm WHERE bm.member_id = :memberId")
    List<BadgeMember> findByMemberId(@Param("memberId") UUID memberId);
}
