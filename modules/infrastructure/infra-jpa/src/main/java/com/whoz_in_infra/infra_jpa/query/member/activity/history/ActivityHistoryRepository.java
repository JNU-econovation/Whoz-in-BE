package com.whoz_in_infra.infra_jpa.query.member.activity.history;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivityHistoryRepository extends JpaRepository<ActivityHistory, Long> {

    @Query("SELECT a FROM ActivityHistory a WHERE a.memberId IN :memberIds AND a.timeUnit = com.whoz_in_infra.infra_jpa.query.member.activity.history.TimeUnit.TOTAL")
    List<ActivityHistory> findTotalByMemberIdIn(@Param("memberIds") Collection<UUID> memberIds);

    Collection<ActivityHistory> findByMemberIdAndReferenceDateGreaterThanEqualAndReferenceDateLessThanEqualAndTimeUnit(
            UUID memberId,
            LocalDate startDate,
            LocalDate endDate,
            TimeUnit timeUnit
    );
}
