package com.whoz_in.api_query_jpa.member.event;

import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import com.whoz_in.api_query_jpa.shared.service.MemberConnectionService;
import com.whoz_in.main_api.shared.domain.member.event.DayEndEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberDayEndEventHandler {

    private final MemberRepository memberRepository;
    private final MemberConnectionService connectionService;
    private final MemberConnectionInfoRepository connectionInfoRepository;

    @EventListener(DayEndEvent.class)
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public void handle(DayEndEvent event) {
        // 기기가 없는 사용자들을 찾는다.
        List<Member> members = memberRepository.findNotHaveAnyDevice();

        if(!members.isEmpty()) {
            log.info("[DayEndEvent] 발생 : 모든 기기를 삭제한 회원의 접속 시간 정보를 초기화 합니다.");

            List<MemberConnectionInfo> connectionInfos =
                    connectionInfoRepository.findByMemberIds(members.stream().map(Member::getId).toList());

            connectionInfos.forEach(connectionInfo -> {
                // 기기가 없는 사용자들은 active 상태가 될 수 없으므로, 호출할 필요가 없다.
//            connectionService.disconnectMember(connectionInfo.getMemberId(), disConnectedAt);
                connectionService.updateTotalTime(connectionInfo.getMemberId());
            });
            connectionInfoRepository.saveAll(connectionInfos);
        }
    }


}
