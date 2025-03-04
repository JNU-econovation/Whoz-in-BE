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
    Optional<MemberInfo> findNameByMemberId(String memberId);
    Optional<MemberConnectionInfo> findConnectionInfo(String memberId);
    Optional<MemberDetailInfo> findDetailByMemberId(UUID memberId);
    List<MemberConnectionInfo> findAllMemberConnectionInfo();
    List<MemberInfo> findAllMemberInfo();
    Long countActiveMember();
}
