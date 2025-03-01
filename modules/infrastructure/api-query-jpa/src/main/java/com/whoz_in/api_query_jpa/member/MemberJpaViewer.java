package com.whoz_in.api_query_jpa.member;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.main_api.query.member.application.view.MemberAuthInfo;
import com.whoz_in.main_api.query.member.application.view.MemberDetailInfo;
import com.whoz_in.main_api.query.member.application.view.MemberConnectionInfo;
import com.whoz_in.main_api.query.member.application.view.MemberInfo;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberJpaViewer implements MemberViewer {
    private final MemberRepository repository;
    private final ActiveDeviceRepository activeDeviceRepository;
    private final MemberConnectionInfoRepository connectionInfoRepository;

    @Override
    public Optional<MemberAuthInfo> findAuthInfoByLoginId(String loginId) {
        return repository.findByLoginId(loginId)
                .map(member -> new MemberAuthInfo(member.getId(), member.getLoginId(), member.getEncodedPassword()));
    }

    @Override
    public Optional<MemberInfo> findNameByMemberId(String memberId) {
        return repository.findById(UUID.fromString(memberId))
                .map(member -> new MemberInfo(member.getGeneration(), member.getName()));
    }

    @Override
    public Optional<MemberConnectionInfo> findConnectionInfo(String memberId) {
        return connectionInfoRepository.findByMemberId(UUID.fromString(memberId))
                .map(connectInfo ->
                        new MemberConnectionInfo(
                        connectInfo.getMemberId(),
                        connectInfo.getDailyTime(),
                        connectInfo.getTotalTime(),
                        connectInfo.isActive())
                );
    }

    @Override
    public Optional<MemberDetailInfo> findDetailByMemberId(UUID memberId) {
        return repository.findById(memberId).map(member -> new MemberDetailInfo(memberId, member.getName(), member.getGeneration(), member.getPosition(), member.getStatusMessage()));
    }
}
