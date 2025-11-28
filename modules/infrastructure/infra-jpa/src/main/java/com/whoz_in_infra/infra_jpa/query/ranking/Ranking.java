package com.whoz_in_infra.infra_jpa.query.ranking;

import com.whoz_in.main_api.query.shared.presentation.TimeFormatter;
import java.time.Duration;

public record Ranking(
        String memberId,
        int generation,
        String memberName,
        String content,
        int rank
) {
    public static Ranking ofTime(String memberId, int generation, String memberName, long totalNanos, int rank) {
        return new Ranking(
                memberId,
                generation,
                memberName,
                TimeFormatter.toHour(Duration.ofNanos(totalNanos)),
                rank
        );
    }

    public static Ranking ofDays(String memberId, int generation, String memberName, int totalDays, int rank) {
        return new Ranking(
                memberId,
                generation,
                memberName,
                totalDays + "일",
                rank
        );
    }
}
