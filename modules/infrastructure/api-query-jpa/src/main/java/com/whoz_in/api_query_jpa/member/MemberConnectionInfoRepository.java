package com.whoz_in.api_query_jpa.member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberConnectionInfoRepository extends JpaRepository<MemberConnectionInfo, UUID> {

    Optional<MemberConnectionInfo> findByMemberId(UUID memberId);

    @Query("SELECT count(connectionInfo) FROM MemberConnectionInfo connectionInfo WHERE connectionInfo.isActive = true")
    Long countActiveMember();

    @Query("SELECT connectionInfo FROM MemberConnectionInfo connectionInfo WHERE connectionInfo.memberId IN :memberIds")
    List<MemberConnectionInfo> findByMemberIds(@Param("memberIds") List<UUID> memberIds);

}
