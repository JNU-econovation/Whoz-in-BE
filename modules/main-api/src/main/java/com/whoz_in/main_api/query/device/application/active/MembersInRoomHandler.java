package com.whoz_in.main_api.query.device.application.active;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDevice;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDeviceViewer;
import com.whoz_in.main_api.query.member.application.MemberInfo;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class MembersInRoomHandler implements QueryHandler<MembersInRoom, MembersInRoomResponse> {

    private final ActiveDeviceViewer activeDeviceViewer;
    private final MemberViewer memberViewer;

    @Override
    public MembersInRoomResponse handle(MembersInRoom query) {
        int page = query.page() - 1;
        int size = query.size();
        String sortType = query.sortType();

        List<ActiveDevice> activeDevices = activeDeviceViewer.findAll();
        List<MemberInRoomResponse> responses = new ArrayList<>();

        Map<MemberId, List<ActiveDevice>> activeDevicesByMemberId = createMemberDeviceMap(activeDevices);

        if(!activeDevices.isEmpty()) {

            int start = page * size;
            int end = Math.min((start + size), activeDevicesByMemberId.keySet().size());

            for (int i = start; i < end; i++) {
                ActiveDevice activeDevice = activeDevices.get(i);
                MemberInfo ownerInfo = getMemberName(activeDevice.memberId().toString());

                String memberId = activeDevice.memberId().toString();
                int generation = ownerInfo.generation();
                String memberName = ownerInfo.memberName();
                Long continuousMinute = activeDevice.continuousTime().toMinutes();
                Long totalConnectedMinute = activeDevice.totalConnectedTime().toMinutes();
                boolean isActive = activeDevice.isActive();

                MemberInRoomResponse oneResponse = new MemberInRoomResponse(
                        generation,
                        memberId,
                        memberName,
                        String.format("%s시간 %s분", continuousMinute / 60, continuousMinute % 60),
                        String.format("%s시간 %s분", totalConnectedMinute / 60, totalConnectedMinute % 60),
                        isActive
                );
                responses.add(oneResponse);
            }

            // TODO : 정렬 자동화
            if (sortType.equals("asc"))
                responses.sort(Comparator.comparing(MemberInRoomResponse::memberName));
            else
                responses.sort(Comparator.comparing(MemberInRoomResponse::totalActiveTime));

            return new MembersInRoomResponse(responses);
        }

        return new MembersInRoomResponse(responses);
    }

    private Map<MemberId, List<ActiveDevice>> createMemberDeviceMap(List<ActiveDevice> activeDevices) {
        Set<UUID> memberIds = new HashSet<>();
        activeDevices.forEach(activeDevice -> memberIds.add(activeDevice.memberId()));
        return memberIds.stream()
                .collect(Collectors.toMap(
                        MemberId::new,
                        memberId -> {
                            return activeDevices.stream()
                                    .filter(device -> device.memberId().equals(memberId))
                                    .collect(Collectors.toList());
                        }
                ));
    }

    private MemberInfo getMemberName(String memberId){
        return memberViewer.findNameByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 기기와 사용자 이름 매핑 중 예상치 못한 에러 발생"));
    }
}
