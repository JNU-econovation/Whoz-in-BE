package com.whoz_in.main_api.query.member.application.shared;

import com.whoz_in.main_api.query.shared.application.Viewer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberInfoViewer extends Viewer {
    Optional<MemberInfoView> findByMemberId(UUID memberId);
    List<MemberInfoView> findAll();
}
