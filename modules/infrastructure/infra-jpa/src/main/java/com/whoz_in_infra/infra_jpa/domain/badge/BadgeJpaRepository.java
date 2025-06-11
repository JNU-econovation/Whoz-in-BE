package com.whoz_in_infra.infra_jpa.domain.badge;

import com.whoz_in.domain.badge.BadgeRepository;
import com.whoz_in.domain.badge.model.Badge;
import com.whoz_in.domain.badge.model.BadgeId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BadgeJpaRepository implements BadgeRepository {
    private final BadgeEntityRepository badgeRepo; //이거 도메인 아니고 JpaRepo
    private final BadgeConverter converter;
    @Override
    public void save(Badge badge) {
        badgeRepo.save(converter.from(badge));
    }

    @Override
    public Optional<Badge> findByName(String name) {
        return badgeRepo.findByName(name).map(converter::to);
    }

    @Override
    public Optional<Badge> findByBadgeId(BadgeId badgeId) {
        return badgeRepo.findOneById(badgeId.id()).map(converter::to);
    }
}
