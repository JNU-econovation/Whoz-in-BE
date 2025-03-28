package com.whoz_in.api_query_jpa.shared.service;

import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import java.time.Duration;
import java.time.LocalDateTime;
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
    public void disconnectMember(UUID memberId, LocalDateTime disconnectedAt) {

        // connection 정보 조회
        Optional<MemberConnectionInfo> memberConnectionInfo = connectionInfoRepository.findByMemberId(memberId);

        if(memberConnectionInfo.isPresent()) {
            MemberConnectionInfo connectionInfo = memberConnectionInfo.get();

            updateDailyTime(connectionInfo, disconnectedAt);
            return;
        }

        log.warn("회원가입 할 때, memberConnectionInfo 가 만들어지지 않음 (memberId) : {}", memberId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void connectMember(UUID memberId, LocalDateTime time) {
        Optional<MemberConnectionInfo> memberConnectionInfo = connectionInfoRepository.findByMemberId(memberId);

        if(memberConnectionInfo.isPresent()) {
            MemberConnectionInfo connectionInfo = memberConnectionInfo.get();

            if(!connectionInfo.isActive()) {
                log.info("connect (memberId) : {}", connectionInfo.getMemberId());
                connectionInfo.activeOn(time);
                connectionInfoRepository.save(connectionInfo);
            }

            return;
        }

        log.warn("회원가입 할 때, memberConnectionInfo 가 만들어지지 않음 (memberId) : {}", memberId);
    }

    private void updateDailyTime(MemberConnectionInfo connectionInfo, LocalDateTime disConnectedAt) {
        try {
            // 선 비활성화
            connectionInfo.inActiveOn(disConnectedAt);

            LocalDateTime inActiveAt = connectionInfo.getInActiveAt();
            LocalDateTime activeAt = connectionInfo.getActiveAt();

//            if(Objects.isNull(activeAt)) activeAt = LocalDateTime.now();
//            if(Objects.isNull(inActiveAt)) inActiveAt = LocalDateTime.now();

            Duration continuousTime = Duration.between(activeAt, inActiveAt).abs();

            // 후 DailyTime 계산
            connectionInfo.addDailyTime(continuousTime);

            connectionInfoRepository.save(connectionInfo);

            log.info("disconnect (memberId) : {}", connectionInfo.getMemberId());
        } catch (Exception e) {
            log.warn("[예상치 못한 에러로, dailyTime 업데이트 실패]");
            log.warn("exception : {}", e);
            throw e;
        }
    }

    /**
     *
     * @param memberId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateTotalTime(UUID memberId) {
        // connection 정보 조회
        Optional<MemberConnectionInfo> memberConnectionInfo = connectionInfoRepository.findByMemberId(memberId);

        if(memberConnectionInfo.isPresent()) {
            MemberConnectionInfo connectionInfo = memberConnectionInfo.get();
            connectionInfo.addTotalTime();
            connectionInfo.resetDailyTime();
            connectionInfoRepository.save(connectionInfo);
            log.info("updateTotalTime (memberId) : {}", connectionInfo.getMemberId());
        }

    }

}
