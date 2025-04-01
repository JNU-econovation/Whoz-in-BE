package com.whoz_in.api_query_jpa.member;

import com.whoz_in.api_query_jpa.badge.BadgeRepository;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.main_api.query.badge.application.view.BadgeInfo;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.query.member.application.view.MemberAuthInfo;
import com.whoz_in.main_api.query.member.application.view.MemberConnectionInfo;
import com.whoz_in.main_api.query.member.application.view.MemberDetailInfo;
import com.whoz_in.main_api.query.member.application.view.MemberInfo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberJpaViewer implements MemberViewer {
    private final MemberRepository repository;
    private final BadgeRepository badgeRepository;
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
    public List<MemberConnectionInfo> findByMemberIds(List<UUID> memberIds) {
        return connectionInfoRepository.findByMemberIds(memberIds).stream().map(this::toMemberConnectionInfo).toList();
    }

    @Override
    public List<MemberInfo> findAllMemberInfoOrderByStatus() {
        return repository.findAllOrderByStatus().stream().map(this::toMemberInfo).toList();
    }

    @Override
    public List<MemberInfo> findMembersByStatus(boolean isActive) {
        return repository.findByStatus(isActive).stream().map(this::toMemberInfo).toList();
    }

    @Override
    public Long countActiveMember() {
        return connectionInfoRepository.countActiveMember();
    }

    private MemberConnectionInfo toMemberConnectionInfo(com.whoz_in.api_query_jpa.member.MemberConnectionInfo entity){
        return new MemberConnectionInfo(entity.getMemberId(), entity.getDailyTime(), entity.getTotalTime(), entity.getActiveAt(), entity.getInActiveAt(), entity.isActive());
    }

    private MemberInfo toMemberInfo(com.whoz_in.api_query_jpa.member.Member entity){
        List<BadgeInfo> badge = getBadgeInfo(entity);
        BadgeInfo representativeBadge = getRepresentativeBadge(entity);
        return new MemberInfo(entity.getId(),
                entity.getPosition(),
                entity.getStatusMessage(),
                entity.getGeneration(),
                entity.getName(), badge, representativeBadge);
    }

    private MemberAuthInfo toMemberAuthInfo(com.whoz_in.api_query_jpa.member.Member entity){
        return new MemberAuthInfo(entity.getId(), entity.getLoginId(), entity.getEncodedPassword());
    }

    private MemberDetailInfo toMemberDetailInfo(com.whoz_in.api_query_jpa.member.Member entity){
        return new MemberDetailInfo(entity.getId(), entity.getName(), entity.getGeneration(), entity.getPosition(), entity.getStatusMessage());
    }

    private List<BadgeInfo> getBadgeInfo(com.whoz_in.api_query_jpa.member.Member entity){
        return badgeRepository.findBadgesByMemberId(entity.getId()).stream(
                ).map(badge -> new BadgeInfo(badge.getName(), badge.getColor_code(), badge.getDescription())).toList();
    }

    private BadgeInfo getRepresentativeBadge(com.whoz_in.api_query_jpa.member.Member entity){
        return badgeRepository.findRepresentativeBadge(entity.getId()).map(badge -> new BadgeInfo(badge.getName(), badge.getColor_code(), badge.getDescription()))
                .orElseThrow(() -> new IllegalStateException("Representative badge not found for member: " + entity.getId()));
    }


}
