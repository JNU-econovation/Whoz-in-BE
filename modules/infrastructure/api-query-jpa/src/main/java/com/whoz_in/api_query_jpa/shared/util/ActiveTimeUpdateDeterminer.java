package com.whoz_in.api_query_jpa.shared.util;

import com.whoz_in.api_query_jpa.device.DeviceRepository;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveTimeUpdateDeterminer {

    private final ActiveDeviceRepository activeDeviceRepository;
    private final DeviceRepository deviceRepository;
    private final MemberRepository memberRepository;

    /**
     *
     * @param deviceId
     * @return 이 Device 가 ActiveTime 을 업데이트 할 수 있는 대상인지 T/F
     */
    public boolean isUpdatable(UUID deviceId){

        // 1. 현재 시간이 자정인가.
        //  a. 자정이라면) 가장 오랫동안 있었는가.
        //  b. 자정이 아니라면) 마지막으로 접속 종료된 기기인가.

        if(nowIsMidNight()){
            return isLongest(deviceId);
        }

        return isLastDevice(deviceId);
    }

    private boolean nowIsMidNight(){
        LocalDateTime now = LocalDateTime.now();
        return now.toLocalTime().equals(LocalTime.MIDNIGHT);
    }

    private boolean isLongest(UUID deviceId) {
        // 가장 오랫동안 있었는지 검증
        Member owner = findOwner(deviceId);

        if(owner==null) return false;

        List<ActiveDeviceEntity> activeDevices = findMyActiveDevices(owner.getId());

        UUID longestDeviceId = findLongestDevice(activeDevices);

        return deviceId.equals(longestDeviceId);
    }

    private boolean isLastDevice(UUID deviceId) {
        // 마지막으로 접속 종료된 기기인지 검증

        Member owner = findOwner(deviceId);

        if(owner==null) return false;

        List<ActiveDeviceEntity> myDevices = findMyActiveDevices(owner.getId());

        UUID lastDevice = findLastDevice(myDevices);

        return lastDevice.equals(deviceId);

    }

    private List<ActiveDeviceEntity> findMyActiveDevices(UUID ownerId){
        return activeDeviceRepository.findByMemberId(ownerId);
    }

    private Member findOwner(UUID deviceId){
        return memberRepository.findByDeviceId(deviceId).orElse(null);
    }

    private UUID findLongestDevice(List<ActiveDeviceEntity> activeDevices){
        // 접속 시간을 기준으로 가장 오래된 기기를 찾는다.
        return activeDevices.stream()
                .max((activeDevice1, activeDevice2) -> {
                    Duration ad1continuousTime = Duration.between(activeDevice1.getConnectedAt(), activeDevice1.getDisConnectedAt()).abs();
                    Duration ad2continuousTime = Duration.between(activeDevice2.getConnectedAt(), activeDevice2.getDisConnectedAt()).abs();
                    return ad1continuousTime.compareTo(ad2continuousTime);
                })
                .map(ActiveDeviceEntity::getDeviceId)
                .orElse(null);
    }

    private UUID findLastDevice(List<ActiveDeviceEntity> activeDevices){
        // disconnect 시간을 기준으로 가장 늦게 연결이 종료된 기기 리턴
        return activeDevices.stream()
                .max((activeDevice1, activeDevice2) -> {
                    LocalDateTime ad1DisconnectedAt = activeDevice1.getDisConnectedAt();
                    LocalDateTime ad2DisconnectedAt = activeDevice2.getDisConnectedAt();
                    if(ad1DisconnectedAt.isEqual(ad2DisconnectedAt)) return 0;
                    if(ad1DisconnectedAt.isAfter(ad2DisconnectedAt)) return 1;
                    return -1;
                })
                .map(ActiveDeviceEntity::getDeviceId)
                .orElse(null);
    }


}
