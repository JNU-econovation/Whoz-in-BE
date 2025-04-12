package com.whoz_in.api_query_jpa.member;

import com.whoz_in.api_query_jpa.member.activity.daily.DailyActivityStatusService;
import com.whoz_in.main_api.query.member.application.shared.MemberActivityView;
import com.whoz_in.main_api.query.member.application.shared.MemberActivityViewer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MemberActivityJpaViewer implements MemberActivityViewer {
    private final DailyActivityStatusService statusService;
    @Override
    public List<MemberActivityView> findAll() {
        return statusService.getList().stream().map(s-> new MemberActivityView(
                s.getMemberId(),
                s.isActive(),
                s.getActiveTime()
            )).toList();
    }
}
