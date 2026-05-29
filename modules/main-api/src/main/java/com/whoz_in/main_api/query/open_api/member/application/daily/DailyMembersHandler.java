package com.whoz_in.main_api.query.open_api.member.application.daily;

import static com.whoz_in.main_api.shared.statics.DeviceConnectionStatics.UPDATE_TERM_MINUTE;

import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

@Handler
@RequiredArgsConstructor
public class DailyMembersHandler implements QueryHandler<com.whoz_in.main_api.query.api.member.application.daily.DailyMembersGet, com.whoz_in.main_api.query.api.member.application.daily.DailyMembers> {
    private final MemberInfoViewer memberInfoViewer;
    private final TodayActivityViewer todayActivityViewer;
    private final RequesterInfo requesterInfo;
    private volatile List<com.whoz_in.main_api.query.api.member.application.daily.DailyMember> cachedMembers; // 여러 스레드에서 동시에 읽기/쓰기가 가능하므로 volatile

    @EventListener(ApplicationReadyEvent.class)
    protected void init() {
        refresh(); // 초기화 시 멤버 현황을 채운다.
    }

    @Override
    public com.whoz_in.main_api.query.api.member.application.daily.DailyMembers handle(com.whoz_in.main_api.query.api.member.application.daily.DailyMembersGet query) {
        return filter(query);
    }

    private com.whoz_in.main_api.query.api.member.application.daily.DailyMembers filter(DailyMembersGet query) {
        int start = page * size;
        int end = Math.min(start + size, cachedMembers.size());
        List<com.whoz_in.main_api.query.api.member.application.daily.DailyMember> paged = cachedMembers.subList(start, end);
        int activeCount = (int) cachedMembers.stream().filter(com.whoz_in.main_api.query.api.member.application.daily.DailyMember::isActive).count();
        return new DailyMembers(paged, activeCount);
    }

    @Scheduled(fixedRate = 60, timeUnit = TimeUnit.MINUTES)
    protected void refresh() {
        Map<UUID, TodayActivityView> activities = todayActivityViewer.findAll().stream()
                .collect(Collectors.toMap(TodayActivityView::memberId, activity -> activity));

        this.cachedMembers = memberInfoViewer.findAll().stream()
                .map(info -> {
                    TodayActivityView activityView = activities.get(info.memberId());
                    return new DailyMember(info, activityView);
                })
                .sorted(
                        // 오늘 동방에 온 사람 우선
                        Comparator.comparing(com.whoz_in.main_api.query.api.member.application.daily.DailyMember::hasBeenActive).reversed()
                                // active인 사람 우선
                                .thenComparing(com.whoz_in.main_api.query.api.member.application.daily.DailyMember::isActive, Comparator.reverseOrder())
                                // activeTime이 큰 순
                                .thenComparing(com.whoz_in.main_api.query.api.member.application.daily.DailyMember::todayActiveTime, Comparator.reverseOrder())
                                // generation 내림차순
                                .thenComparing(com.whoz_in.main_api.query.api.member.application.daily.DailyMember::generation, Comparator.reverseOrder())
                                // 이름순 정렬
                                .thenComparing(DailyMember::memberName)
                )
                .toList();
    }

}
