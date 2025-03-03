package com.whoz_in.api_query_jpa.shared.service;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
// 데이터 정합성은 문제가 없다고 가정
// Device - ActiveDevice
// Member - MemberConnectionInfo
public class MemberConnectionService {
    
    private final MemberConnectionInfoRepository connectionInfoRepository;
    private final MemberRepository memberRepository;
    private final DeviceService deviceService;
    private final TimeUpdateService timeUpdateService;

    /**
     * inActive 처리된 기기 주인을 disconnect 시킨다.
     * (필요한 데이터는 다 있다고 가정.)
     * 
     * 1. 어떤 멤버가 접속 종료가 되면, 마지막으로 접속이 종료된 기기를 찾는다.
     * 2. 찾은 기기에서 연속 접속 시간을 가져온다.
     * 3. connection 정보에 반영한다.
     * @param memberId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean disconnectMember(UUID memberId) {

        // connection 정보 조회
        Optional<MemberConnectionInfo> memberConnectionInfo = connectionInfoRepository.findByMemberId(memberId);

        if(memberConnectionInfo.isPresent()) {
            Optional<ActiveDeviceEntity> activeDevice = deviceService.findLastConnectedDevice(memberId);

            // active device 정보 조회
            if(activeDevice.isPresent()){
                ActiveDeviceEntity ad = activeDevice.get();
                MemberConnectionInfo connectionInfo = memberConnectionInfo.get();

                return updateDailyTime(ad, connectionInfo);
            }

        }
        return false;
    }

    private boolean updateDailyTime(ActiveDeviceEntity activeDevice, MemberConnectionInfo connectionInfo) {
        try {
            Duration continuousTime = Duration.between(activeDevice.getConnectedAt(), activeDevice.getDisConnectedAt()).abs();

            connectionInfo.inActiveOn();
            connectionInfo.addDailyTime(continuousTime);

            connectionInfoRepository.save(connectionInfo);
        } catch (Exception e) {
            log.warn("[예상치 못한 에러로, dailyTime 업데이트 실패");
            log.warn("exception : {}", e.getMessage());
            return false;
        }
        return true;
    }

}
