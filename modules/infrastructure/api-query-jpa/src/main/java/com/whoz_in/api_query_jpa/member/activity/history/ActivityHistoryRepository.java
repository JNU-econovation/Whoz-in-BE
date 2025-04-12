package com.whoz_in.api_query_jpa.member.activity.history;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActivityHistoryRepository extends JpaRepository<ActivityHistory, Long> {

    @Query("SELECT a FROM ActivityHistory a WHERE a.memberId IN :memberIds AND a.timeUnit = com.whoz_in.api_query_jpa.member.activity.history.TimeUnit.TOTAL")
    List<ActivityHistory> findTotalByMemberIdIn(Collection<UUID> memberIds);
}
