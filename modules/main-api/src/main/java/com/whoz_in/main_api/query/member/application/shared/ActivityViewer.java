package com.whoz_in.main_api.query.member.application.shared;

import com.whoz_in.main_api.query.shared.application.Viewer;
import java.time.LocalDate;
import java.util.UUID;

public interface ActivityViewer extends Viewer {
    // 미래를 호출하더라도 빈 값으로 반환
    ActivitiesView findAllByMemberIdBetween(UUID memberId, LocalDate start, LocalDate inclusiveEnd);
}
