package com.whoz_in.main_api.query.api.ranking.application;

import com.whoz_in.main_api.query.shared.presentation.TimeFormatter;
import java.time.Duration;

public record RankingMember (
        String memberId,
        int generation,
        String memberName,
        String content, // 시간, 일수 등
        int rank,
        boolean isMe
){
        // Duration -> "N시간"
        public static RankingMember forTime(String memberId, int generation, String memberName, long totalNanos, int rank, boolean isMe) {
                return new RankingMember(
                        memberId,
                        generation,
                        memberName,
                        TimeFormatter.toHour(Duration.ofNanos(totalNanos)),
                        rank,
                        isMe
                );
        }

        // 일수 -> "N일"
        public static RankingMember forDays(String memberId, int generation, String memberName, int days, int rank, boolean isMe) {
                return new RankingMember(
                        memberId,
                        generation,
                        memberName,
                        days + "일",
                        rank,
                        isMe
                );
        }
}
