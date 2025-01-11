package com.whoz_in.domain_jpa.badge;

import com.whoz_in.domain.badge.BadgeRepository;
import com.whoz_in.domain.badge.model.Badge;
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
}
