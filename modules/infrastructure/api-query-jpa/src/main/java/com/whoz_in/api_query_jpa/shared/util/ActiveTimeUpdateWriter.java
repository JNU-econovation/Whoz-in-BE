package com.whoz_in.api_query_jpa.shared.util;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 이 클래스의 메소드는 모두 UUID deviceId 를 인자로 받는다.
 * 전달받은 deviceId 로 찾는 ActiveDevice 들은 모두 InActive 상태이다.
 * InActive 상태가 아니면, 연속 접속시간, 하루 접속시간, 총 누적 접속 시간을 계산할 수 없다.
 * 또한 모든 기기는,
 */
@Component
@RequiredArgsConstructor
public class ActiveTimeUpdateWriter {

    private final MemberConnectionInfoRepository connectionInfoRepository;
    private final ActiveDeviceRepository activeDeviceRepository;
    private final MemberRepository memberRepository;
    private final ActiveTimeUpdateDeterminer updateDeterminer;

    // deviceId를 넘겨주고, 이 기기에 대해서 접속 정보를 업데이트 해도 되는지 판별해주는 클래스가 있어야 한다.

    // 해당 기기 주인의 dailyTime 을 update
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDailyTime(UUID deviceId){
        if(updateDeterminer.isUpdatable(deviceId)){
            ActiveDeviceEntity activeDevice = activeDeviceRepository.findByDeviceId(deviceId).orElse(null);
            MemberConnectionInfo connectionInfo = findOwnerConnectionInfoByDeviceId(deviceId);

            if(connectionInfo!=null && activeDevice != null){
                Duration continuousTime = Duration.between(activeDevice.getConnectedAt(), activeDevice.getDisConnectedAt()).abs();
                connectionInfo.addDailyTime(continuousTime);
                connectionInfoRepository.save(connectionInfo);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateTotalActiveTime(UUID deviceId){
        if(updateDeterminer.isUpdatable(deviceId)){
            MemberConnectionInfo connectionInfo = findOwnerConnectionInfoByDeviceId(deviceId);

            if(connectionInfo!=null){
                connectionInfo.addTotalTime();
                connectionInfo.resetDailyTime();
                connectionInfoRepository.save(connectionInfo);
            }

        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void clearDailyTime(UUID deviceId){
        Member owner = memberRepository.findByDeviceId(deviceId).orElse(null);
        if(owner==null) return;

        MemberConnectionInfo connectionInfo = connectionInfoRepository.findByMemberId(owner.getId()).orElse(null);
        if(connectionInfo==null) return;

        connectionInfo.resetDailyTime();;
    }

    private MemberConnectionInfo findOwnerConnectionInfoByDeviceId(UUID deviceId){
        return activeDeviceRepository.findByDeviceId(deviceId)
                .map(ActiveDeviceEntity::getDeviceId)
                .flatMap(memberRepository::findByDeviceId)
                .map(Member::getId)
                .flatMap(connectionInfoRepository::findByMemberId)
                .orElse(null);

    }

}
