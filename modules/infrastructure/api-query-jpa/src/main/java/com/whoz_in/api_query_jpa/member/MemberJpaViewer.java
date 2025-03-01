package com.whoz_in.api_query_jpa.member;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.main_api.query.member.application.view.MemberAuthInfo;
import com.whoz_in.main_api.query.member.application.view.MemberDetailInfo;
import com.whoz_in.main_api.query.member.application.view.MemberConnectionInfo;
import com.whoz_in.main_api.query.member.application.view.MemberInfo;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import java.util.List;
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
                .map(this::toMemberAuthInfo);
    }

    @Override
    public Optional<MemberInfo> findNameByMemberId(String memberId) {
        return repository.findById(UUID.fromString(memberId))
                .map(this::toMemberInfo);
    }

    @Override
    public Optional<MemberConnectionInfo> findConnectionInfo(String memberId) {
        return connectionInfoRepository.findByMemberId(UUID.fromString(memberId))
                .map(this::toMemberConnectionInfo);
    }

    @Override
    public Optional<MemberDetailInfo> findDetailByMemberId(UUID memberId) {
        return repository.findById(memberId).map(this::toMemberDetailInfo);
    }

    @Override
    public List<MemberConnectionInfo> findAllMemberConnectionInfo() {
        return connectionInfoRepository.findAll().stream().map(this::toMemberConnectionInfo).toList();
    }

    @Override
    public List<MemberInfo> findAllMemberInfo() {
        return repository.findAll().stream().map(this::toMemberInfo).toList();
    }

    private MemberConnectionInfo toMemberConnectionInfo(com.whoz_in.api_query_jpa.member.MemberConnectionInfo entity){
        return new MemberConnectionInfo(entity.getMemberId(), entity.getDailyTime(), entity.getTotalTime(), entity.isActive());
    }

    private MemberInfo toMemberInfo(com.whoz_in.api_query_jpa.member.Member entity){
        return new MemberInfo(entity.getId(), entity.getPosition(), entity.getStatusMessage(), entity.getGeneration(), entity.getName());
    }

    private MemberAuthInfo toMemberAuthInfo(com.whoz_in.api_query_jpa.member.Member entity){
        return new MemberAuthInfo(entity.getId(), entity.getLoginId(), entity.getEncodedPassword());
    }

    private MemberDetailInfo toMemberDetailInfo(com.whoz_in.api_query_jpa.member.Member entity){
        return new MemberDetailInfo(entity.getId(), entity.getName(), entity.getGeneration(), entity.getPosition(), entity.getStatusMessage());
    }
}
