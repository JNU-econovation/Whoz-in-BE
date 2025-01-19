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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class MemberListHandler implements QueryHandler<MemberList, MemberListResponse> {

    private final ActiveDeviceViewer activeDeviceViewer;
    private final MemberViewer memberViewer;

    @Override
    public MemberListResponse handle(MemberList query) {
        int page = query.page() - 1;
        int size = query.size();
        String sortType = query.sortType();

        List<ActiveDevice> activeDevices = activeDeviceViewer.findAll();
        List<MemberResponse> responses = new ArrayList<>();

        Map<MemberId, List<ActiveDevice>> activeDevicesByMemberId = createMemberDeviceMap(activeDevices);

        if(!activeDevices.isEmpty()) {

            int start = page * size;
            int end = Math.min((start + size), activeDevices.size());

            for (int i = start; i < end; i++) {
                ActiveDevice activeDevice = activeDevices.get(i);
                MemberInfo ownerInfo = getMemberName(activeDevice.memberId().toString());

                String memberId = activeDevice.memberId().toString();
                int generation = ownerInfo.generation();
                String memberName = ownerInfo.memberName();
                Long continuousMinute = activeDevice.continuousTime().toMinutes();
                Long totalConnectedMinute = activeDevice.totalConnectedTime().toMinutes();
                boolean isActive = activeDevice.isActive();

                MemberResponse oneResponse = new MemberResponse(
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
                responses.sort(Comparator.comparing(MemberResponse::memberName));
            else
                responses.sort(Comparator.comparing(MemberResponse::totalActiveTime));

            return new MemberListResponse(responses);
        }

        return new MemberListResponse(responses);
    }

    private Map<MemberId, List<ActiveDevice>> createMemberDeviceMap(List<ActiveDevice> activeDevices) {
        Map<MemberId, List<ActiveDevice>> activeDevicesByMemberId = activeDevices.stream()
                .collect(Collectors.toMap(
                        activeDevice -> new MemberId(activeDevice.memberId()),
                        activeDevice -> {
                            MemberId memberId = new MemberId(activeDevice.memberId());
                            return activeDevices.stream()
                                    .filter(device -> device.memberId().equals(memberId.id()))
                                    .collect(Collectors.toList());
                        }
                ));

        return activeDevicesByMemberId;
    }

    private MemberInfo getMemberName(String memberId){
        return memberViewer.findNameByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 기기와 사용자 이름 매핑 중 예상치 못한 에러 발생"));
    }
}
