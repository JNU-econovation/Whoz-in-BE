package com.whoz_in.main_api.query.member.application.query;

import static com.whoz_in.main_api.query.member.application.support.ConnectionTimeFormatter.hourMinuteTime;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.query.device.application.DeviceCount;
import com.whoz_in.main_api.query.device.application.DeviceViewer;
import com.whoz_in.main_api.query.device.exception.RegisteredDeviceCountException;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.query.member.application.response.MemberInRoomResponse;
import com.whoz_in.main_api.query.member.application.response.MemberInRoomResponse.Badge;
import com.whoz_in.main_api.query.member.application.response.MembersInRoomResponse;
import com.whoz_in.main_api.query.member.application.view.MemberConnectionInfo;
import com.whoz_in.main_api.query.member.application.view.MemberInfo;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.domain.device.active.event.DeviceCreatedEvent;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import com.whoz_in.main_api.shared.utils.Sorter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

@Handler
@RequiredArgsConstructor
public class MembersInRoomHandler implements QueryHandler<MembersInRoom, MembersInRoomResponse> {
    private final MemberViewer memberViewer;
    private final DeviceViewer deviceViewer;
    private final RequesterInfo requesterInfo;
    private volatile List<MemberInRoomResponse> cachedMembers; // 여러 스레드에서 동시에 읽기/쓰기가 가능하므로 volatile

    @Override
    public MembersInRoomResponse handle(MembersInRoom query) {
        validateRegisteredDeviceCount(requesterInfo.getMemberId());
        return filtering(query);
    }

    private void validateRegisteredDeviceCount(MemberId memberId) {
        DeviceCount count = deviceViewer.findDeviceCount(memberId.id());
        if(count.value()<1) throw RegisteredDeviceCountException.EXCEPTION;
    }

    private MembersInRoomResponse filtering(MembersInRoom query) {
        // TODO: 애플리케이션에서 정렬하지 말고, DB에서 정렬 후 페이지에 맞는 데이터만 가져오기.
        int page = query.page() - 1;
        int size = query.size();
        String sortType = query.sortType();
        String status = query.status();

        if (cachedMembers == null || cachedMembers.isEmpty()) {
            return new MembersInRoomResponse(List.of(), 0);
        }

        List<MemberInRoomResponse> filtered = cachedMembers.stream()
                .filter(m -> {
                    if ("active".equals(status)) return m.isActive();
                    else if ("inactive".equals(status)) return !m.isActive();
                    else return true;
                })
                .collect(Collectors.toList());

        if ("asc".equals(sortType)) {
            Sorter.<MemberInRoomResponse>builder()
                    .comparator(Comparator.comparing(MemberInRoomResponse::isActive).reversed())
                    .comparator(Comparator.comparing(MemberInRoomResponse::dailyActiveMinute).reversed())
                    .comparator(Comparator.comparing(MemberInRoomResponse::generation))
                    .comparator(Comparator.comparing(MemberInRoomResponse::memberName))
                    .build()
                    .sort(filtered);
        } else {
            Sorter.<MemberInRoomResponse>builder()
                    .comparator(Comparator.comparing(MemberInRoomResponse::isActive))
                    .comparator(Comparator.comparing(MemberInRoomResponse::dailyActiveMinute))
                    .comparator(Comparator.comparing(MemberInRoomResponse::generation).reversed())
                    .comparator(Comparator.comparing(MemberInRoomResponse::memberName).reversed())
                    .build()
                    .sort(filtered);
        }

        int start = page * size;
        int end = Math.min(start + size, filtered.size());
        List<MemberInRoomResponse> paged = filtered.subList(start, end);
        int activeCount = (int) cachedMembers.stream().filter(MemberInRoomResponse::isActive).count();
        return new MembersInRoomResponse(paged, activeCount);
    }

    @EventListener(DeviceCreatedEvent.class) // TODO: device가 쿼리에 적용됐을때 하는게 베스트
    @Scheduled(fixedRate = 1000 * 60) // 1분마다 현황 업데이트하니까 1분마다 함
    private void updateMembers(){
        Map<UUID, MemberInfo> memberInfoMap = memberViewer.findAllMemberInfoOrderByStatus().stream()
                .collect(Collectors.toMap(MemberInfo::memberId, mi -> mi));

        List<UUID> memberIds = memberInfoMap.keySet().stream().toList();

        Map<UUID, MemberConnectionInfo> memberConnectionInfoMap = memberViewer.findByMemberIds(memberIds).stream()
                .collect(Collectors.toMap(
                        MemberConnectionInfo::memberId,
                        memberConnectionInfo -> memberConnectionInfo
                ));

        this.cachedMembers = memberIds.stream()
                .map(memberId ->
                    toResponse(
                            memberInfoMap.get(memberId),
                            memberConnectionInfoMap.get(memberId)
                    )
                )
                .toList();
    }

    private MemberInRoomResponse toResponse(MemberInfo ownerInfo, MemberConnectionInfo connectionInfo) {
        Long continuousMinute = connectionInfo.continuousTime().toMinutes();
        Long dailyConnectedMinute = getDailyConnectedTime(connectionInfo, continuousMinute);
        return new MemberInRoomResponse(
                ownerInfo.generation(),
                ownerInfo.memberId().toString(),
                ownerInfo.memberName(),
                hourMinuteTime(continuousMinute),
                hourMinuteTime(dailyConnectedMinute),
                new Badge(ownerInfo.repBadge()),
                dailyConnectedMinute,
                connectionInfo.isActive()
        );
    }

    private Long getDailyConnectedTime(MemberConnectionInfo connectionInfo, Long continuousMinute){
        boolean isActive = connectionInfo.isActive();
        return isActive ? connectionInfo.dailyTime().toMinutes() + continuousMinute : connectionInfo.dailyTime().toMinutes();
    }
}
