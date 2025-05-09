package com.whoz_in.main_api.query.member.application.shared;

import com.whoz_in.main_api.query.shared.application.Viewer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodayActivityViewer extends Viewer {
    // 오늘 재실 멤버들의 정보를 가져온다.
    List<TodayActivityView> findAll();
    Optional<TodayActivityView> findByMemberId(UUID memberId);
}
