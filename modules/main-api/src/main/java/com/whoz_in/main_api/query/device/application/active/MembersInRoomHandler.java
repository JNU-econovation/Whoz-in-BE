package com.whoz_in.main_api.query.device.application.active;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.query.device.application.DeviceOwner;
import com.whoz_in.main_api.query.device.application.DeviceCount;
import com.whoz_in.main_api.query.device.application.DevicesStatus;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDevice;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDeviceViewer;
import com.whoz_in.main_api.query.device.view.DeviceViewer;
import com.whoz_in.main_api.query.member.application.MemberConnectionInfo;
import com.whoz_in.main_api.query.device.exception.RegisteredDeviceCountException;
import com.whoz_in.main_api.query.device.view.DeviceViewer;
import com.whoz_in.main_api.query.member.application.MemberInfo;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.time.Duration;
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
    private final DeviceViewer deviceViewer;
    private final RequesterInfo requesterInfo;

    @Override
    public MembersInRoomResponse handle(MembersInRoom query) {
        validateRegisteredDeviceCount(requesterInfo.getMemberId());

        int page = query.page() - 1;
        int size = query.size();
        String sortType = query.sortType();

        List<MemberInRoomResponse> responses = new ArrayList<>();

        List<ActiveDevice> activeDevices = activeDeviceViewer.findAll();
        Map<MemberId, List<ActiveDevice>> activeDevicesByMemberId = createMemberDeviceMap(activeDevices);

        if(!activeDevices.isEmpty()) {

            int start = page * size;
            int end = Math.min((start + size), activeDevicesByMemberId.keySet().size());

            List<MemberId> memberIds = activeDevicesByMemberId.keySet().stream().toList();

            for (int i = start; i < end; i++) {

                MemberId memberId = memberIds.get(i);
                List<ActiveDevice> activeDevicesByMember = activeDevicesByMemberId.get(memberId);
                MemberConnectionInfo connectionInfo = memberViewer.findConnectionInfo(memberId.id().toString()).get();

                MemberInRoomResponse oneResponse = toResponse(memberId, activeDevicesByMember, connectionInfo);

                responses.add(oneResponse);
            }

            // TODO : 정렬 자동화
            if (sortType.equals("asc"))
                responses.sort(Comparator.comparing(MemberInRoomResponse::memberName));
            else
                responses.sort(Comparator.comparing(MemberInRoomResponse::totalActiveTime));

            return new MembersInRoomResponse(responses, (int)responses.stream().filter(MemberInRoomResponse::isActive).count());
        }

        return new MembersInRoomResponse(responses, 0);
    }

    private void validateRegisteredDeviceCount(MemberId memberId) {
        DeviceCount count = deviceViewer.findDeviceCount(memberId.id());
        if(count.value()<1) throw RegisteredDeviceCountException.EXCEPTION;
    }

    private MemberInRoomResponse toResponse(MemberId memberId, List<ActiveDevice> devices, MemberConnectionInfo connectionInfo){
        MemberInfo ownerInfo = getMemberName(memberId.id().toString());

        int generation = ownerInfo.generation();
        String memberName = ownerInfo.memberName();

        // active 기기가 여러 개라면, 여러 기기 중, 가장 큰 연속 접속 시간만 보여준다.
        Long continuousMinute = devices.stream()
                .filter(ActiveDevice::isActive)
                .map(ActiveDevice::continuousTime)
                .max(Duration::compareTo)
                .orElse(Duration.ZERO)
                .toMinutes();

        Long totalConnectedMinute = connectionInfo.totalTime().toMinutes();
        boolean isActive = connectionInfo.isActive();

        // 1. 여러 기기 중, 연속 접속 시간, 누적 접속 시간을 합한 정보를 보여준다.

        return new MemberInRoomResponse(
                generation,
                memberId.id().toString(),
                memberName,
                String.format("%s시간 %s분", continuousMinute / 60, continuousMinute % 60),
                String.format("%s시간 %s분", totalConnectedMinute / 60, totalConnectedMinute % 60),
                isActive
        );
    }

    private Map<MemberId, List<ActiveDevice>> createMemberDeviceMap(List<ActiveDevice> activeDevices) {
        Set<UUID> memberIds = new HashSet<>();
        activeDevices.forEach(activeDevice -> memberIds.add(findDeviceOwnerId(activeDevice.deviceId())));
        return memberIds.stream()
                .collect(Collectors.toMap(
                        MemberId::new,
                        memberId -> {
                            return activeDevices.stream()
                                    .filter(device -> findDeviceOwnerId(device.deviceId()).equals(memberId))
                                    .collect(Collectors.toList());
                        }
                ));
    }

    private UUID findDeviceOwnerId(UUID deviceId){
        return deviceViewer.findDeviceOwner(deviceId).ownerId();
    }

    private MemberInfo getMemberName(String memberId){
        return memberViewer.findNameByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 기기와 사용자 이름 매핑 중 예상치 못한 에러 발생"));
    }
}
