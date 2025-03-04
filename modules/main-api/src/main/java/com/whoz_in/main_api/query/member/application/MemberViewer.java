package com.whoz_in.main_api.query.member.application;

import com.whoz_in.main_api.query.member.application.view.MemberAuthInfo;
import com.whoz_in.main_api.query.member.application.view.MemberConnectionInfo;
import com.whoz_in.main_api.query.member.application.view.MemberDetailInfo;
import com.whoz_in.main_api.query.member.application.view.MemberInfo;
import com.whoz_in.main_api.query.shared.application.Viewer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberViewer extends Viewer {
    Optional<MemberAuthInfo> findAuthInfoByLoginId(String loginId);
    Optional<MemberInfo> findNameByMemberId(String memberId); // TODO: UUID로 변경
    Optional<MemberConnectionInfo> findConnectionInfo(String memberId); // TODO: UUID로 변경
    Optional<MemberDetailInfo> findDetailByMemberId(UUID memberId);
    List<MemberConnectionInfo> findAllMemberConnectionInfo();
    List<MemberConnectionInfo> findByMemberIds(List<UUID> memberIds);
    List<MemberInfo> findAllMemberInfo();
    List<MemberInfo> findMembersByStatus(boolean isActive); //TODO: 상태 관리 Enum 으로 하기
    Long countActiveMember();
}
