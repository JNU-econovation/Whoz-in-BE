package com.whoz_in.api_query_jpa.member;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberConnectionInfoRepository extends JpaRepository<MemberConnectionInfo, UUID> {

    Optional<MemberConnectionInfo> findByMemberId(UUID memberId);

}
