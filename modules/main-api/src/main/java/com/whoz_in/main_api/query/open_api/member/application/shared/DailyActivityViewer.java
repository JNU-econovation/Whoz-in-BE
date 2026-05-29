package com.whoz_in.main_api.query.open_api.member.application.shared;

import com.whoz_in.main_api.query.shared.application.Viewer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DailyActivityViewer extends Viewer {
    // 오늘 재실 멤버들의 정보를 가져온다.
    List<DailyActivityView> findAll();
    Optional<DailyActivityView> findByMemberId(UUID memberId);
}
