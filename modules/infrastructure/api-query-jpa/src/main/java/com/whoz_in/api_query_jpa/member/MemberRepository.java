package com.whoz_in.api_query_jpa.member;

import com.whoz_in.main_api.query.member.application.view.MemberInfo;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByLoginId(String loginId);

    @Query("SELECT m FROM Member m WHERE m.id IN (SELECT d.memberId FROM Device d WHERE d.id = :deviceId)")
    Optional<Member> findByDeviceId(@Param("deviceId") UUID deviceId);

    @Query("SELECT m FROM Member m WHERE m.id IN (SELECT d.memberId FROM Device d WHERE d.id IN (:deviceIds))")
    List<Member> findByDeviceIds(@Param("deviceIds") List<UUID> deviceIds);

    @Query("SELECT m FROM Member m WHERE m.id IN (SELECT connectionInfo.memberId FROM MemberConnectionInfo connectionInfo WHERE connectionInfo.isActive = :status)")
    List<Member> findByStatus(@Param("status") boolean status);

    default Member getByDeviceId(UUID deviceId) {
        return findByDeviceId(deviceId).orElseThrow(() -> new IllegalArgumentException("기기의 주인을 찾을 수 없음"));
    }

    @Query("SELECT m FROM Member m JOIN MemberConnectionInfo mci WHERE m.id=mci.memberId ORDER BY mci.isActive asc")
    List<Member> findAllOrderByStatus();

}
