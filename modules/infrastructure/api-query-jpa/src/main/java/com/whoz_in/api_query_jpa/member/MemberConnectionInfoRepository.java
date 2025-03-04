package com.whoz_in.api_query_jpa.member;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberConnectionInfoRepository extends JpaRepository<MemberConnectionInfo, UUID> {

    Optional<MemberConnectionInfo> findByMemberId(UUID memberId);

    @Query("SELECT connectionInfo FROM MemberConnectionInfo connectionInfo WHERE connectionInfo.isActive = true")
    Long countActiveMember();

}
