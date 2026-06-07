package com.whoz_in_infra.infra_jpa.query.member.activity;

import com.whoz_in.main_api.query.open_api.member.application.by_date.MembersByDateViewer;
import com.whoz_in.main_api.query.open_api.member.application.by_date.MembersOnDateView;
import com.whoz_in.main_api.query.open_api.member.application.by_date.MembersOnDateView.MemberPresence;
import com.whoz_in.shared.DayBoundaryUtil;
import com.whoz_in_infra.infra_jpa.query.member.Member;
import com.whoz_in_infra.infra_jpa.query.member.MemberRepository;
import com.whoz_in_infra.infra_jpa.query.member.activity.history.ActivityHistory;
import com.whoz_in_infra.infra_jpa.query.member.activity.history.ActivityHistoryRepository;
import com.whoz_in_infra.infra_jpa.query.member.activity.history.TimeUnit;
import com.whoz_in_infra.infra_jpa.query.member.activity.today.TodayActivity;
import com.whoz_in_infra.infra_jpa.query.member.activity.today.TodayActivityService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MembersByDateJpaViewer implements MembersByDateViewer {
    private final ActivityHistoryRepository activityHistoryRepository;
    private final TodayActivityService todayActivityService;
    private final MemberRepository memberRepository;

    @Override
    public MembersOnDateView findAllByDate(LocalDate date) {
        Collection<ActivityHistory> histories = activityHistoryByDate(date);
        List<TodayActivity> todayActivities = todayActivitiesByDate(date);
        Map<UUID, Member> membersById = membersById(histories, todayActivities);
        return new MembersOnDateView(
                date,
                toMemberPresenceViews(histories, todayActivities, membersById).stream()
                        .sorted(memberComparator())
                        .toList()
        );
    }

    @Override
    public List<MembersOnDateView> findAllByYearMonth(YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        Collection<ActivityHistory> histories = activityHistoryRepository
                .findByReferenceDateGreaterThanEqualAndReferenceDateLessThanEqualAndTimeUnit(startDate, endDate, TimeUnit.DAY);
        Map<LocalDate, List<ActivityHistory>> historiesByDate = histories.stream()
                .collect(Collectors.groupingBy(ActivityHistory::getReferenceDate));
        Map<LocalDate, List<TodayActivity>> todayActivitiesByDate = todayActivitiesByDate(startDate, endDate);
        Map<UUID, Member> membersById = membersById(histories, todayActivitiesByDate.values().stream().flatMap(List::stream).toList());

        return java.util.stream.Stream.concat(historiesByDate.keySet().stream(), todayActivitiesByDate.keySet().stream())
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        date -> toMemberPresenceViews(
                                historiesByDate.getOrDefault(date, List.of()),
                                todayActivitiesByDate.getOrDefault(date, List.of()),
                                membersById
                        )
                ))
                .entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new MembersOnDateView(
                        entry.getKey(),
                        entry.getValue().stream()
                                .sorted(memberComparator())
                                .toList()
                ))
                .toList();
    }

    private Collection<ActivityHistory> activityHistoryByDate(LocalDate date) {
        return activityHistoryRepository.findByReferenceDateGreaterThanEqualAndReferenceDateLessThanEqualAndTimeUnit(
                date,
                date,
                TimeUnit.DAY
        );
    }

    private List<TodayActivity> todayActivitiesByDate(LocalDate date) {
        LocalDate today = DayBoundaryUtil.today();
        if (!today.equals(date)) {
            return List.of();
        }
        return todayActivityService.findAll();
    }

    private Map<LocalDate, List<TodayActivity>> todayActivitiesByDate(LocalDate startDate, LocalDate endDate) {
        LocalDate today = DayBoundaryUtil.today();
        if (today.isBefore(startDate) || today.isAfter(endDate)) {
            return Map.of();
        }
        return Map.of(today, todayActivityService.findAll());
    }

    private Map<UUID, Member> membersById(Collection<ActivityHistory> histories, Collection<TodayActivity> todayActivities) {
        List<UUID> memberIds = histories.stream()
                .map(ActivityHistory::getMemberId)
                .collect(Collectors.toSet())
                .stream()
                .toList();
        List<UUID> todayMemberIds = todayActivities.stream()
                .map(TodayActivity::getMemberId)
                .toList();

        return memberRepository.findAllById(
                        java.util.stream.Stream.concat(memberIds.stream(), todayMemberIds.stream())
                                .distinct()
                                .toList()
                ).stream()
                .collect(Collectors.toMap(Member::getId, Function.identity()));
    }

    private List<MemberPresence> toMemberPresenceViews(
            Collection<ActivityHistory> histories,
            Collection<TodayActivity> todayActivities,
            Map<UUID, Member> membersById
    ) {
        List<MemberPresence> historyMembers = histories.stream()
                .map(history -> toMemberPresenceView(history.getMemberId(), history.getActiveTime(), membersById))
                .filter(java.util.Objects::nonNull)
                .toList();
        List<MemberPresence> todayMembers = todayActivities.stream()
                .map(todayActivity -> toMemberPresenceView(todayActivity.getMemberId(), todayActivity.getActiveTime(), membersById))
                .filter(java.util.Objects::nonNull)
                .toList();

        return java.util.stream.Stream.concat(historyMembers.stream(), todayMembers.stream()).toList();
    }

    private MemberPresence toMemberPresenceView(UUID memberId, java.time.Duration activeTime, Map<UUID, Member> membersById) {
        Member member = membersById.get(memberId);
        if (member == null) {
            return null;
        }
        return new MemberPresence(
                memberId,
                member.getGeneration(),
                member.getName(),
                activeTime
        );
    }

    private Comparator<MemberPresence> memberComparator() {
        return Comparator.comparing(MemberPresence::presenceDuration, Comparator.reverseOrder())
                .thenComparing(MemberPresence::generation, Comparator.reverseOrder())
                .thenComparing(MemberPresence::memberName);
    }
}
