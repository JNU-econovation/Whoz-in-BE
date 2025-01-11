package com.whoz_in.domain.badge;

import com.whoz_in.domain.badge.model.Badge;
import java.util.Optional;

public interface BadgeRepository {
    void save(Badge badge);
    Optional<Badge> findByName(String name);
}
