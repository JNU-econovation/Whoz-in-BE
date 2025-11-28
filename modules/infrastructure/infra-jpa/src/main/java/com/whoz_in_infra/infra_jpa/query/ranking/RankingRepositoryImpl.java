package com.whoz_in_infra.infra_jpa.query.ranking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepository {
    private final EntityManager em;
    // 10분 이상인 경우 출석으로 인정
    private static final long MIN_ATTENDANCE_NANOS = 10L * 60 * 1_000_000_000L;

    @Override
    public List<Ranking> findTimeRanking(LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT BIN_TO_UUID(m.id) as member_id,
                       m.generation,
                       m.name,
                       SUM(h.active_time) as total_time
                FROM activity_history h
                JOIN member_entity m ON h.member_id = m.id
                WHERE h.time_unit = 'DAY'
                  AND h.reference_date BETWEEN ?1 AND ?2
                GROUP BY m.id, m.generation, m.name
                ORDER BY total_time DESC
                """;

        Query query = em.createNativeQuery(sql)
                .setParameter(1, startDate)
                .setParameter(2, endDate);

        List<Object[]> results = query.getResultList();

        AtomicInteger rankCounter = new AtomicInteger(1);

        return results.stream()
                .map(row -> {
                    String memberId = (String) row[0];
                    int generation = ((Number) row[1]).intValue();
                    String memberName = (String) row[2];
                    BigDecimal totalVal = (BigDecimal) row[3];
                    long totalNanos = totalVal.longValue();

                    return Ranking.ofTime(
                            memberId,
                            generation,
                            memberName,
                            totalNanos,
                            rankCounter.getAndIncrement()
                    );
                })
                .toList();
    }

    @Override
    public List<Ranking> findMaxStreakRanking() {
        String sql = """
            WITH valid_days AS (
                SELECT member_id, reference_date,
                       ROW_NUMBER() OVER (PARTITION BY member_id ORDER BY reference_date) as rn
                FROM activity_history
                WHERE time_unit = 'DAY' AND active_time >= ?1
            ),
            streak_groups AS (
                SELECT member_id, DATE_SUB(reference_date, INTERVAL rn DAY) as grp
                FROM valid_days
            ),
            all_streaks AS (
                SELECT member_id, COUNT(*) as streak_days
                FROM streak_groups
                GROUP BY member_id, grp
            ),
            best_streaks AS (
                SELECT member_id, MAX(streak_days) as max_days
                FROM all_streaks
                GROUP BY member_id
            )
            SELECT BIN_TO_UUID(m.id) as member_id,
                   m.generation,
                   m.name,
                   bs.max_days
            FROM best_streaks bs
            JOIN member_entity m ON bs.member_id = m.id
            ORDER BY bs.max_days DESC
            """;

        Query query = em.createNativeQuery(sql)
                .setParameter(1, MIN_ATTENDANCE_NANOS);

        return mapToDaysRankingWithTies(query.getResultList());
    }

    @Override
    public List<Ranking> findTotalTimeRanking() {
        String sql = """
                SELECT BIN_TO_UUID(m.id) as member_id,
                       m.generation,
                       m.name,
                       h.active_time
                FROM activity_history h
                JOIN member_entity m ON h.member_id = m.id
                WHERE h.time_unit = 'TOTAL'
                ORDER BY h.active_time DESC
                """;

        Query query = em.createNativeQuery(sql);
        List<Object[]> results = query.getResultList();

        AtomicInteger rankCounter = new AtomicInteger(1);

        return results.stream()
                .map(row -> {
                    String memberId = (String) row[0];
                    int generation = ((Number) row[1]).intValue();
                    String memberName = (String) row[2];

                    BigDecimal totalVal = (BigDecimal) row[3];
                    long totalNanos = totalVal.longValue();

                    return Ranking.ofTime(
                            memberId,
                            generation,
                            memberName,
                            totalNanos,
                            rankCounter.getAndIncrement()
                    );
                })
                .toList();
    }

    @Override
    public List<Ranking> findTotalAttendanceRanking() {
        String sql = """
                SELECT BIN_TO_UUID(m.id) as member_id,
                       m.generation,
                       m.name,
                       COUNT(*) as total_days
                FROM activity_history h
                JOIN member_entity m ON h.member_id = m.id
                WHERE h.time_unit = 'DAY'
                  AND h.active_time >= ?1
                GROUP BY m.id, m.generation, m.name
                ORDER BY total_days DESC
                """;

        Query query = em.createNativeQuery(sql)
                .setParameter(1, MIN_ATTENDANCE_NANOS);

        return mapToDaysRankingWithTies(query.getResultList());
    }

    private List<Ranking> mapToDaysRankingWithTies(List<Object[]> results) {
        List<Ranking> rankingMembers = new ArrayList<>();
        int currentRank = 1;
        int previousDays = -1;

        for (int i = 0; i < results.size(); i++) {
            Object[] row = results.get(i);

            String memberId = (String) row[0];
            int generation = ((Number) row[1]).intValue();
            String memberName = (String) row[2];

            int days = ((Number) row[3]).intValue();

            // 1, 1, 3 랭킹 로직
            if (i > 0 && days != previousDays) {
                currentRank = i + 1;
            }

            rankingMembers.add(Ranking.ofDays(
                    memberId,
                    generation,
                    memberName,
                    days,
                    currentRank
            ));

            previousDays = days;
        }
        return rankingMembers;
    }
}
