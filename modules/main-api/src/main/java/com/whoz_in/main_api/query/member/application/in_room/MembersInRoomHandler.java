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
import com.whoz_in.main_api.shared.utils.TimeFormatter;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@Handler
@RequiredArgsConstructor
public class MembersInRoomHandler implements QueryHandler<MembersInRoomGet, MembersInRoom> {
    private final MemberInfoViewer memberInfoViewer;
    private final TodayActivityViewer todayActivityViewer;
    private final DeviceViewer deviceViewer;
    private final RequesterInfo requesterInfo;
    private volatile List<MemberInRoom> cachedMembers; // 여러 스레드에서 동시에 읽기/쓰기가 가능하므로 volatile

    @PostConstruct
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
        // TODO: 애플리케이션에서 정렬하지 말고, DB에서 정렬 후 페이지에 맞는 데이터만 가져올수도
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
        // TODO: 정렬
        this.cachedMembers = memberInfoViewer.findAll().stream()
                .map(info -> {
                    TodayActivityView activityView = activities.get(info.memberId());
                    if (activityView == null) return null;
                    return new MemberInRoom(
                            info.memberId().toString(),
                            info.generation(),
                            info.name(),
                            info.mainBadgeName(),
                            info.mainBadgeColor(),
                            TimeFormatter.hourMinuteTime(activityView.activeTime()),
                            activityView.isActive()
                    );
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
