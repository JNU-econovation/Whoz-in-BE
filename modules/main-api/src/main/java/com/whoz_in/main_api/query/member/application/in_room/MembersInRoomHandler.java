package com.whoz_in.main_api.query.member.application.in_room;

import static com.whoz_in.main_api.shared.statics.DeviceConnectionStatics.UPDATE_TERM_MINUTE;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.query.device.application.DeviceCount;
import com.whoz_in.main_api.query.device.application.DeviceViewer;
import com.whoz_in.main_api.query.device.exception.RegisteredDeviceCountException;
import com.whoz_in.main_api.query.member.application.shared.MemberInfoViewer;
import com.whoz_in.main_api.query.member.application.shared.TodayActivityView;
import com.whoz_in.main_api.query.member.application.shared.TodayActivityViewer;
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
public class MembersInRoomHandler implements QueryHandler<MembersInRoomGet, MembersInRoom> {
    private final MemberInfoViewer memberInfoViewer;
    private final TodayActivityViewer todayActivityViewer;
    private final DeviceViewer deviceViewer;
    private final RequesterInfo requesterInfo;
    private volatile List<MemberInRoom> cachedMembers; // 여러 스레드에서 동시에 읽기/쓰기가 가능하므로 volatile

    @EventListener(ApplicationReadyEvent.class)
    protected void init() {
        refresh(); // 초기화 시 멤버 현황을 채운다.
    }

    @Override
    public MembersInRoom handle(MembersInRoomGet query) {
        validateRegisteredDeviceCount(requesterInfo.getMemberId());
        return filter(query);
    }

    private void validateRegisteredDeviceCount(MemberId memberId) {
        DeviceCount count = deviceViewer.countDevice(memberId.id());
        if(count.value()<1) throw RegisteredDeviceCountException.EXCEPTION;
    }

    private MembersInRoom filter(MembersInRoomGet query) {
        int page = query.page() - 1;
        int size = query.size();
        int start = page * size;
        int end = Math.min(start + size, cachedMembers.size());
        List<MemberInRoom> paged = cachedMembers.subList(start, end);
        int activeCount = (int) cachedMembers.stream().filter(MemberInRoom::isActive).count();
        return new MembersInRoom(paged, activeCount);
    }

    @Scheduled(fixedRate = UPDATE_TERM_MINUTE, timeUnit = TimeUnit.MINUTES)
    protected void refresh() {
        Map<UUID, TodayActivityView> activities = todayActivityViewer.findAll().stream()
                .collect(Collectors.toMap(TodayActivityView::memberId, activity -> activity));

        this.cachedMembers = memberInfoViewer.findAll().stream()
                .map(info -> {
                    TodayActivityView activityView = activities.get(info.memberId());
                    return new MemberInRoom(info, activityView);
                })
                .sorted(
                        // 오늘 동방에 온 사람 우선
                        Comparator.comparing(MemberInRoom::hasBeenActive).reversed()
                                // active인 사람 우선
                                .thenComparing(MemberInRoom::isActive, Comparator.reverseOrder())
                                // activeTime이 큰 순
                                .thenComparing(MemberInRoom::todayActiveTime, Comparator.reverseOrder())
                                // generation 내림차순
                                .thenComparing(MemberInRoom::generation, Comparator.reverseOrder())
                                // 이름순 정렬
                                .thenComparing(MemberInRoom::memberName)
                )
                .toList();
    }

}
