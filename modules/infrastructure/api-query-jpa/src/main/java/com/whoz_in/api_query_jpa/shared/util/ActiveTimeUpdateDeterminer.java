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
    private final DeviceUtils deviceUtils;

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
            return deviceUtils.isLongest(deviceId);
        }

        return deviceUtils.isLastDevice(deviceId);
    }

    private boolean nowIsMidNight(){
        LocalDateTime now = LocalDateTime.now();
        return now.toLocalTime().equals(LocalTime.MIDNIGHT);
    }

}
