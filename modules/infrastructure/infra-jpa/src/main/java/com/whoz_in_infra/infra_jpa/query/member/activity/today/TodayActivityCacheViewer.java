package com.whoz_in_infra.infra_jpa.query.member.activity.today;

import com.whoz_in.main_api.query.member.application.shared.TodayActivityView;
import com.whoz_in.main_api.query.member.application.shared.TodayActivityViewer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 캐시로 구현된 {@link TodayActivityService}를 이용하기 때문에
 * CacheViewer라고 작명했습니다.
*/

@Component
@RequiredArgsConstructor
public class TodayActivityCacheViewer implements TodayActivityViewer {
    private final TodayActivityService statusService;
    @Override
    public List<TodayActivityView> findAll() {
        return statusService.findAll().stream().map(s-> new TodayActivityView(
                s.getMemberId(),
                s.isActive(),
                s.getActiveTime()
            )).toList();
    }

    @Override
    public Optional<TodayActivityView> findByMemberId(UUID memberId) {
        return statusService.findOne(memberId).map(
                status-> new TodayActivityView(memberId, status.isActive(), status.getActiveTime())
        );
    }
}
