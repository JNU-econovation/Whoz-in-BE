package com.whoz_in.main_api.query.member.application;

import com.whoz_in.main_api.query.shared.application.Viewer;
import java.util.Optional;
import java.util.UUID;

public interface MemberViewer extends Viewer {
    Optional<MemberAuthInfo> findAuthInfoByLoginId(String loginId);
    Optional<MemberInfo> findNameByMemberId(String memberId);
    Optional<MemberDetailInfo> findDetailByMemberId(UUID memberId);
}
