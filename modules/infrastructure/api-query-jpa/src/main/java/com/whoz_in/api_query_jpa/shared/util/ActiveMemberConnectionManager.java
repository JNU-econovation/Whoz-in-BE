package com.whoz_in.api_query_jpa.shared.util;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// ActiveDeviceEntity 가 발견되면, 이에 대응되는 MemberConnectionInfo 를 Active 시켜주는 친구
@Component
@RequiredArgsConstructor
public class ActiveMemberConnectionManager {

    private final MemberRepository memberRepository;
    private final MemberConnectionInfoRepository connectionInfoRepository;

    public void memberConnectionOn(List<ActiveDeviceEntity> entities) {
        List<MemberConnectionInfo> connectionInfos = entities.stream()
                .map(ActiveDeviceEntity::getDeviceId)
                .map(memberRepository::findByDeviceId)
                .map(opt -> opt.orElse(null)).filter(Objects::nonNull)
                .map(Member::getId)
                .map(connectionInfoRepository::findByMemberId)
                .map(opt -> opt.orElse(null)).filter(Objects::nonNull)
                .peek(MemberConnectionInfo::activeOn)
                .toList();

        connectionInfoRepository.saveAll(connectionInfos);
    }

}
