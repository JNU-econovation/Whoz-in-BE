package com.whoz_in_infra.infra_jpa.query.ranking;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepository {
    List<Ranking> findTimeRanking(LocalDate startDate, LocalDate endDate);
    List<Ranking> findMaxStreakRanking();
    List<Ranking> findTotalTimeRanking();
    List<Ranking> findTotalAttendanceRanking();
}
