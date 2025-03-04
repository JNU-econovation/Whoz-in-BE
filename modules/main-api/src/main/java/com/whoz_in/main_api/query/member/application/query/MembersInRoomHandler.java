package com.whoz_in.main_api.query.member.application.query;

import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.query.device.application.DeviceCount;
import com.whoz_in.main_api.query.device.application.DevicesStatus.DeviceStatus;
import com.whoz_in.main_api.query.member.application.response.MemberInRoomResponse;
import com.whoz_in.main_api.query.member.application.response.MembersInRoomResponse;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDevice;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDeviceViewer;
import com.whoz_in.main_api.query.device.view.DeviceViewer;
import com.whoz_in.main_api.query.member.application.view.MemberConnectionInfo;
import com.whoz_in.main_api.query.device.exception.RegisteredDeviceCountException;
import com.whoz_in.main_api.query.member.application.view.MemberInfo;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import com.whoz_in.main_api.shared.utils.Sorter;
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
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class MembersInRoomHandler implements QueryHandler<MembersInRoom, MembersInRoomResponse> {

    private final ActiveDeviceViewer activeDeviceViewer;
    private final MemberViewer memberViewer;
    private final DeviceViewer deviceViewer;
    private final RequesterInfo requesterInfo;

    @Override
    @Transactional // TODO: 병렬 스트림 내부에서 발생하는 Lazy 로딩 예외 방지를 위한 트랜잭셔널
    public MembersInRoomResponse handle(MembersInRoom query) {
        validateRegisteredDeviceCount(requesterInfo.getMemberId());

        int page = query.page() - 1;
        int size = query.size();
        String sortType = query.sortType();
        String status = query.status();

        // 응답용 리스트
        List<MemberInRoomResponse> responses = new ArrayList<>();

        // 상태에 맞는 회원 정보 조회
        List<MemberInfo> memberInfos = findByStatus(status);

        // 해당 멤버 접속 정보 조회
        List<MemberConnectionInfo> memberConnectionInfos = memberViewer.findByMemberIds(memberInfos.stream().map(MemberInfo::memberId).toList());

        // 해당 회원의 기기 정보 조회
        Map<MemberId, List<DeviceStatus>> devicesStatusByMemberId = createDevicesStatusMap(memberInfos); // Map<MemberId, List<DeviceStatus>>

        List<DeviceId> deviceIds = devicesStatusByMemberId.values().stream()
                .flatMap(List::stream)
                .map(DeviceStatus::deviceId)
                .map(DeviceId::new)
                .toList();

        // 해당 회원의 ActiveDevice 정보 조회
        List<ActiveDevice> activeDevices = activeDeviceViewer.findByDeviceIds(deviceIds.stream().map(DeviceId::id).map(UUID::toString).toList());

        Map<MemberId, MemberConnectionInfo> memberConnectionInfoByMemberId = createMemberConnectionInfoMap(memberConnectionInfos);

        Map<MemberId, List<ActiveDevice>> activeDevicesByMemberId = createMemberDeviceMap(activeDevices);

        if(!activeDevices.isEmpty()) {

            int start = page * size;
            int end = Math.min((start + size), memberInfos.size());

            List<MemberId> memberIds = memberInfos.stream().map(MemberInfo::memberId).map(MemberId::new).toList();

            for (int i = start; i < end; i++) {

                MemberId memberId = memberIds.get(i);
                List<DeviceStatus> deviceStatuses = devicesStatusByMemberId.get(memberId);

                if(!deviceStatuses.isEmpty()) {

                    List<ActiveDevice> activeDevicesByMember = activeDevicesByMemberId.get(memberId);
                    MemberConnectionInfo connectionInfo = memberConnectionInfoByMemberId.get(memberId);

                    MemberInRoomResponse oneResponse = toResponse(memberId, activeDevicesByMember, connectionInfo);

                    responses.add(oneResponse);
                }
                // 어떠한 기기도 등록하지 않은 사용자일 경우
                else {
                    responses.add(MemberInRoomResponse.nonDeviceRegisterer(
                            memberInfos.get(i).generation(),
                            memberInfos.get(i).memberId().toString(),
                            memberInfos.get(i).memberName()));
                }
            }

            // TODO : 정렬 자동화
            if (sortType.equals("asc"))
                Sorter.<MemberInRoomResponse>builder()
                        .comparator(Comparator.comparing(MemberInRoomResponse::isActive).reversed())
                        .comparator(Comparator.comparing(MemberInRoomResponse::totalActiveTime))
                        .comparator(Comparator.comparing(MemberInRoomResponse::memberName))
                        .build()
                        .sort(responses);

            else
                Sorter.<MemberInRoomResponse>builder()
                        .comparator(Comparator.comparing(MemberInRoomResponse::isActive).reversed())
                        .comparator(Comparator.comparing(MemberInRoomResponse::memberName));

            return new MembersInRoomResponse(responses, (int)responses.stream().filter(MemberInRoomResponse::isActive).count());
        }

        return new MembersInRoomResponse(responses, 0);
    }

    private List<MemberInfo> findByStatus(String status) {
        if(status.equals("all")){
            return memberViewer.findAllMemberInfo();
        }
        else if(status.equals("active")){
            return memberViewer.findMembersByStatus(true);
        }
        else{
            return memberViewer.findMembersByStatus(false);
        }
    }

    private Map<MemberId, MemberConnectionInfo> createMemberConnectionInfoMap(
            List<MemberConnectionInfo> memberConnectionInfos) {

        Set<UUID> memberIds = new HashSet<>();
        memberConnectionInfos.forEach(memberConnectionInfo -> memberIds.add(memberConnectionInfo.memberId()));
        return memberIds.stream()
                .parallel()
                .collect(Collectors.toMap(
                        MemberId::new,
                        memberId -> {
                            return memberConnectionInfos.stream().filter(info -> info.memberId().equals(memberId)).findFirst()
                                    .orElse(new MemberConnectionInfo(memberId, Duration.ZERO, Duration.ZERO, false));
                        }
                ));
    }

    private Map<MemberId, List<DeviceStatus>> createDevicesStatusMap(List<MemberInfo> memberInfos) {

        Set<UUID> memberIds = new HashSet<>();
        memberInfos.forEach(memberInfo -> memberIds.add(memberInfo.memberId()));
        return memberIds.stream()
                .parallel()
                .collect(Collectors.toMap(
                        MemberId::new,
                        memberId -> {
                            return deviceViewer.findDevicesStatus(memberId).devices(); // TODO: 많은 IO 작업 줄이기
                        }
                ));
    }

    private void validateRegisteredDeviceCount(MemberId memberId) {
        DeviceCount count = deviceViewer.findDeviceCount(memberId.id());
        if(count.value()<1) throw RegisteredDeviceCountException.EXCEPTION;
    }

    private MemberInRoomResponse toResponse(MemberId memberId, List<ActiveDevice> devices, MemberConnectionInfo connectionInfo){
        MemberInfo ownerInfo = getMemberName(memberId.id().toString());

        int generation = ownerInfo.generation();
        String memberName = ownerInfo.memberName();
        Long continuousMinute = getContinuousMinute(devices); // active 기기가 여러 개라면, 여러 기기 중, 가장 큰 연속 접속 시간만 보여준다.
        Long dailyConnectedMinute = getDailyConnectedTime(connectionInfo, continuousMinute);
        boolean isActive = connectionInfo.isActive();

        // 1. 여러 기기 중, 연속 접속 시간, 누적 접속 시간을 합한 정보를 보여준다.
        return new MemberInRoomResponse(
                generation,
                memberId.id().toString(),
                memberName,
                String.format("%s시간 %s분", continuousMinute / 60, continuousMinute % 60),
                String.format("%s시간 %s분", dailyConnectedMinute / 60, dailyConnectedMinute % 60),
                isActive
        );
    }

    private Long getContinuousMinute(List<ActiveDevice> activeDevices){
        Long continuousMinute;
        if(activeDevices.size()>1)
            return activeDevices.stream()
                .filter(ActiveDevice::isActive)
                .map(ActiveDevice::continuousTime)
                .max(Duration::compareTo)
                .orElse(Duration.ZERO)
                .toMinutes();

        else
            return activeDevices.stream()
                .map(ActiveDevice::continuousTime)
                .findAny()
                .orElse(Duration.ZERO)
                .toMinutes();

    }

    private Long getDailyConnectedTime(MemberConnectionInfo connectionInfo, Long continuousMinute){
        boolean isActive = connectionInfo.isActive();
        return isActive ? connectionInfo.dailyTime().toMinutes() + continuousMinute : connectionInfo.dailyTime().toMinutes();
    }

    private Map<MemberId, List<ActiveDevice>> createMemberDeviceMap(List<ActiveDevice> activeDevices) {
        Set<UUID> memberIds = new HashSet<>();
        activeDevices.forEach(activeDevice -> memberIds.add(findDeviceOwnerId(activeDevice.deviceId())));
        return memberIds.stream()
                .parallel()
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
