package com.whoz_in_infra.infra_jpa.query.badge;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeMemberRepository extends JpaRepository<BadgeMember, Long> {

    default List<BadgeMember> findAllByMemberId(UUID memberId) {
        return null;
    }
}
