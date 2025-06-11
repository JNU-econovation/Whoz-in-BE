package com.whoz_in_infra.infra_jpa.query.member.activity.history;

import com.whoz_in_infra.infra_jpa.query.device.connection.DeviceConnection;
import com.whoz_in_infra.infra_jpa.query.device.connection.DeviceConnectionUtil;
import com.whoz_in_infra.infra_jpa.query.member.activity.MemberConnectionService;
import com.whoz_in.main_api.shared.utils.TimeRanges;
import com.whoz_in.shared.DayBoundaryUtil;
import com.whoz_in.shared.DayEnded;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// 하루가 끝나면 멤버들의 재실 시간을 ActiveHistory에 기록하는 클래스
@Slf4j
@Component
@RequiredArgsConstructor
public class YesterdayActivityRecorder {
    private final MemberConnectionService memberConnectionService;
    private final ActivityHistoryRepository activityHistoryRepository;

    // 하루가 끝나면 어제 멤버들의 활동 시간을 기록하고 전체 시간에 추가함
    @EventListener(DayEnded.class)
    private void aggregate(DayEnded event) {
        LocalDateTime yesterdayEnd = event.endedAt();
        LocalDateTime yesterdayStart = yesterdayEnd.minusDays(1); // 어제의 하루 시작 시각

        // 멤버별 Connection들
        Map<UUID, List<DeviceConnection>> memberToConnections = memberConnectionService.getMemberConnections(
                yesterdayStart, yesterdayEnd);
        // 멤버별 어제 활동 시간
        Map<UUID, ActivityHistory> dayHistories = calculateYesterday(memberToConnections, yesterdayEnd);
        // 멤버별 총 활동 시간
        List<ActivityHistory> totalHistories = calculateTotal(dayHistories);

        activityHistoryRepository.saveAll(dayHistories.values());
        activityHistoryRepository.saveAll(totalHistories);
    }

    private Map<UUID, ActivityHistory> calculateYesterday(
            Map<UUID, List<DeviceConnection>> memberToConnections,
            LocalDateTime yesterdayEnd
    ){
        return memberToConnections.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            UUID memberId = entry.getKey();
                            TimeRanges timeRanges = DeviceConnectionUtil.toTimeRanges(entry.getValue(), yesterdayEnd);
                            return ActivityHistory.builder()
                                    .memberId(memberId)
                                    .referenceDate(DayBoundaryUtil.yesterday())
                                    .timeUnit(TimeUnit.DAY)
                                    .activeTime(timeRanges.getTotalDuration())
                                    .build();
                        }
                ));
    }

    private List<ActivityHistory> calculateTotal(Map<UUID, ActivityHistory> dayHistories){
        // 멤버별 총 재실 시간 가져와서 멤버 아이디로 매핑
        List<ActivityHistory> existingTotals = activityHistoryRepository.findTotalByMemberIdIn(dayHistories.keySet());
        Map<UUID, ActivityHistory> totals = existingTotals.stream()
                .collect(Collectors.toMap(ActivityHistory::getMemberId, it -> it));

        // 총 활동 시간 있으면 거기에 하루 재실 시간 더하고, 없으면 새로 생성
        return dayHistories.entrySet().stream()
                .map(entry -> {
                    UUID memberId = entry.getKey();
                    Duration dailyDuration = entry.getValue().getActiveTime();
                    ActivityHistory total = totals.get(memberId);
                    if (total != null) {
                        total.add(dailyDuration);
                        return total;
                    } else {
                        return ActivityHistory.builder()
                                .memberId(memberId)
                                .timeUnit(TimeUnit.TOTAL)
                                .referenceDate(DayBoundaryUtil.yesterday())
                                .activeTime(dailyDuration)
                                .build();
                    }
                }).toList();
    }
}
