package com.whoz_in.api_query_jpa.member;

import com.whoz_in.api_query_jpa.member.activity.today.TodayActivityStatusService;
import com.whoz_in.main_api.query.member.application.shared.TodayActivityView;
import com.whoz_in.main_api.query.member.application.shared.TodayActivityViewer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TodayActivityJpaViewer implements TodayActivityViewer {
    private final TodayActivityStatusService statusService;
    @Override
    public List<TodayActivityView> findAll() {
        return statusService.getList().stream().map(s-> new TodayActivityView(
                s.getMemberId(),
                s.isActive(),
                s.getActiveTime()
            )).toList();
    }

    @Override
    public Optional<TodayActivityView> findByMemberId(UUID memberId) {
        return statusService.get(memberId).map(
                status-> new TodayActivityView(memberId, status.isActive(), status.getActiveTime())
        );
    }
}
