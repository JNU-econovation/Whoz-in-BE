package com.whoz_in_infra.infra_jpa.query.ranking;

import com.whoz_in.main_api.query.ranking.application.RankingMember;
import com.whoz_in.main_api.query.ranking.application.RankingViewer;
import com.whoz_in.main_api.shared.enums.RankingType;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingCachedViewer implements RankingViewer {
    private final RankingRepository rankingRepository;
    // 랭킹 데이터
    private volatile Map<RankingType, List<Ranking>> rankingCache = Collections.emptyMap();
    // 기수 목록
    private volatile Map<RankingType, List<Integer>> generationCache = Collections.emptyMap();

    // 상태를 인메모리로 저장하기 때문에 서버 시작 시 초기 상태를 구성한다.
    @EventListener(ApplicationReadyEvent.class)
    private void init(){
        updateRanking();
    }

    // 1시간마다 랭킹 업데이트
    @Scheduled(cron = "0 0 * * * *")
    private void updateRanking() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.with(TemporalAdjusters.lastDayOfMonth());

        Map<RankingType, List<Ranking>> nextRankingCache = new ConcurrentHashMap<>();
        Map<RankingType, List<Integer>> nextGenerationCache = new ConcurrentHashMap<>();

        cacheRankingAndGenerations(nextRankingCache, nextGenerationCache, RankingType.MONTHLY_TIME,
                rankingRepository.findTimeRanking(start, end));
        cacheRankingAndGenerations(nextRankingCache, nextGenerationCache, RankingType.TOTAL_TIME,
                rankingRepository.findTotalTimeRanking());
        cacheRankingAndGenerations(nextRankingCache, nextGenerationCache, RankingType.MAX_STREAK,
                rankingRepository.findMaxStreakRanking());
        cacheRankingAndGenerations(nextRankingCache, nextGenerationCache, RankingType.TOTAL_ATTENDANCE,
                rankingRepository.findTotalAttendanceRanking());

        this.rankingCache = nextRankingCache;
        this.generationCache = nextGenerationCache;
    }

    private void cacheRankingAndGenerations(
            Map<RankingType, List<Ranking>> rCache, Map<RankingType, List<Integer>> gCache,
            RankingType type, List<Ranking> rankings
    ) {
        rCache.put(type, rankings);

        // 기수 목록 추출 (내림차순)
        List<Integer> generations = rankings.stream()
                .map(Ranking::generation)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        gCache.put(type, generations);
    }

    @Override
    public List<Integer> findAvailableGenerations(RankingType type) {
        return generationCache.get(type);
    }

    @Override
    public List<RankingMember> findRanking(RankingType type, int n, Integer generation, UUID finderId) {
        List<Ranking> cachedRankings = rankingCache.getOrDefault(type, Collections.emptyList());

        // 기수 필터링
        List<Ranking> filteredRankings;
        if (generation != null) {
            filteredRankings = cachedRankings.stream()
                    .filter(r -> r.generation() == generation)
                    .toList();
        } else {
            filteredRankings = cachedRankings;
        }

        // Top N + 본인 추출
        String finderIdStr = (finderId != null) ? finderId.toString() : "";
        List<Ranking> targetRankings = extractTargetRankings(filteredRankings, n, finderIdStr);

        return targetRankings.stream()
                .map(ranking -> mapToRankingMember(ranking, finderIdStr))
                .toList();
    }

    private List<Ranking> extractTargetRankings(List<Ranking> rankings, int n, String finderIdStr) {
        if (rankings.isEmpty()) {
            return Collections.emptyList();
        }

        List<Ranking> result = new ArrayList<>();
        boolean meFound = false;
        int limit = Math.min(rankings.size(), n);

        for (int i = 0; i < limit; i++) {
            Ranking ranking = rankings.get(i);
            result.add(ranking);

            if (ranking.memberId().equals(finderIdStr)) {
                meFound = true;
            }
        }

        // 본인이 Top N에 포함되지 않았고, 리스트 뒤쪽에 남아있다면 추가 탐색
        if (!meFound) {
            for (int i = limit; i < rankings.size(); i++) {
                Ranking ranking = rankings.get(i);
                if (ranking.memberId().equals(finderIdStr)) {
                    result.add(ranking);
                    break;
                }
            }
        }

        return result;
    }

    private RankingMember mapToRankingMember(Ranking ranking, String finderIdStr) {
        return new RankingMember(
                ranking.memberId(),
                ranking.generation(),
                ranking.memberName(),
                ranking.content(),
                ranking.rank(),
                ranking.memberId().equals(finderIdStr)
        );
    }
}
