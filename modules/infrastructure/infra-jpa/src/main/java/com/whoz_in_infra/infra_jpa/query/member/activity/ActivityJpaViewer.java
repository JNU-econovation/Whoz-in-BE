package com.whoz_in_infra.infra_jpa.query.member.activity;


import com.whoz_in_infra.infra_jpa.query.member.activity.history.ActivityHistoryRepository;
import com.whoz_in_infra.infra_jpa.query.member.activity.history.TimeUnit;
import com.whoz_in_infra.infra_jpa.query.member.activity.today.TodayActivityService;
import com.whoz_in.main_api.query.member.application.shared.ActivitiesView;
import com.whoz_in.main_api.query.member.application.shared.ActivitiesView.Activity;
import com.whoz_in.main_api.query.member.application.shared.ActivityViewer;
import com.whoz_in.shared.DayBoundaryUtil;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// ActivityHistory는 오늘의 데이터를 가지고 있지 않다는 점을 알면 이해가 쉬울것
@Component
@RequiredArgsConstructor
public class ActivityJpaViewer implements ActivityViewer {
    private final ActivityHistoryRepository historyRepository;
    private final TodayActivityService todayActivityService;

    @Override
    public ActivitiesView findAllByMemberIdBetween(UUID memberId, LocalDate start, LocalDate inclusiveEnd) {
        List<Activity> activities = historyRepository.findByMemberIdAndReferenceDateGreaterThanEqualAndReferenceDateLessThanEqualAndTimeUnit(
                        memberId, start, inclusiveEnd, TimeUnit.DAY
                ).stream()
                .map(history -> new Activity(history.getReferenceDate(), history.getActiveTime()))
                .collect(Collectors.toList());
        LocalDate today = DayBoundaryUtil.today();
        if (!start.isAfter(today) && !inclusiveEnd.isBefore(today)) {
            todayActivityService.findOne(memberId)
                    .map(ta-> new Activity(today, ta.getActiveTime()))
                    .ifPresent(activities::add);
        }
        return new ActivitiesView(activities, activities.stream()
                .map(Activity::activeTime)
                .reduce(Duration.ZERO, Duration::plus));
    }
}
