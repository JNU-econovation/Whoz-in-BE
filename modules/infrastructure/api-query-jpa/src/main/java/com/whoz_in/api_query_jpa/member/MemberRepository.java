package com.whoz_in.api_query_jpa.member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByMemberId(UUID memberId);

    @Query("SELECT m FROM Member m WHERE m.id IN (SELECT d.memberId FROM Device d WHERE d.deviceId = :deviceId)")
    Optional<Member> findByDeviceId(UUID deviceId);

    @Query("SELECT m FROM Member m WHERE m.id IN (SELECT d.memberId FROM Device d WHERE d.deviceId IN (:deviceIds))")
    List<Member> findByDeviceIds(List<UUID> deviceIds);
}
