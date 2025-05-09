package com.whoz_in_infra.infra_jpa.query.member;

import com.whoz_in_infra.infra_jpa.query.member.activity.today.TodayActivity;
import com.whoz_in_infra.infra_jpa.query.member.activity.today.TodayActivityService;
import com.whoz_in.main_api.query.member.application.shared.MemberInfoView;
import com.whoz_in.main_api.query.member.application.shared.MemberInfoViewer;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberInfoJpaViewer implements MemberInfoViewer {
    private final TodayActivityService todayActivityService;
    private final MemberRepository repository;

    @Override
    public Optional<MemberInfoView> findByMemberId(UUID memberId) {
        return repository.findOneById(memberId)
                .map(this::toMemberInfo);
    }

    @Override
    public List<MemberInfoView> findAll() {
        return repository.findAll().stream().map(this::toMemberInfo).toList();
    }

    private MemberInfoView toMemberInfo(Member entity){
        return new MemberInfoView(entity.getId(),
                entity.getGeneration(),
                entity.getName(),
                entity.getPosition(),
                entity.getStatusMessage(),
                entity.getTotalActiveTime().plus(
                        todayActivityService.findOne(entity.getId())
                                .map(TodayActivity::getActiveTime)
                                .orElse(Duration.ZERO)
                ),
                entity.getMainBadgeName(),
                entity.getMainBadgeColor()
        );
    }

}
