package com.whoz_in.api_query_jpa.shared.util;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InActiveMemberConnectionManager {

    private final ActiveTimeUpdateWriter activeTimeUpdateWriter;
    private final ActiveTimeUpdateDeterminer activeTimeUpdateDeterminer;
    private final MemberConnectionInfoRepository connectionInfoRepository;
    private final MemberRepository memberRepository;

    public void processInActiveDevices(List<ActiveDeviceEntity> entities){
        // 기기가 접속이 종료되는 마지막 기기인지 판단하기

        // Member Connection Info 를 update 할 수 있는 조건에 맞는 것만 처리
        entities = entities.stream()
                .filter(activeDevice -> activeTimeUpdateDeterminer.isUpdatable(activeDevice.getDeviceId()))
                .toList();

        updateDailyTime(entities);
        inActiveOn(entities);
    }

    private void updateDailyTime(List<ActiveDeviceEntity> activeDevices){
        List<UUID> deviceIds = activeDevices.stream().map(ActiveDeviceEntity::getDeviceId).toList();
        deviceIds.forEach(activeTimeUpdateWriter::updateDailyTime);
    }

    private void inActiveOn(List<ActiveDeviceEntity> entities) {
        List<MemberConnectionInfo> connectionInfos = entities.stream()
                .map(ActiveDeviceEntity::getDeviceId)
                .map(memberRepository::findByDeviceId)
                .map(opt -> opt.orElse(null)).filter(Objects::nonNull)
                .map(Member::getId)
                .map(connectionInfoRepository::findByMemberId)
                .map(opt -> opt.orElse(null)).filter(Objects::nonNull)
                .peek(MemberConnectionInfo::inActiveOn)
                .toList();

        connectionInfoRepository.saveAll(connectionInfos);
    }

}
