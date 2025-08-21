package com.whoz_in.main_api.query.member.application.block;

import com.whoz_in.main_api.query.member.application.block.MemberBlock.Block;
import com.whoz_in.main_api.query.member.application.shared.ActivitiesView;
import com.whoz_in.main_api.query.member.application.shared.ActivityViewer;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class MemberBlockHandler implements QueryHandler<MemberBlockGet, MemberBlock> {
    private final ActivityViewer activityViewer;

    @Override
    public MemberBlock handle(MemberBlockGet query) {
        // TODO: 후즈인 가입일부터 주기, 캐시 적용(이전 달, 이번 달 기준 다르게) 등 갈아엎어야 함
        LocalDate startDay = query.yearMonth().withDayOfMonth(1); // 해당 달의 1일
        LocalDate endDay = startDay.withDayOfMonth(startDay.lengthOfMonth()); // 해당 달의 마지막 일
        ActivitiesView activities = activityViewer.findAllByMemberIdBetween(query.memberId(), startDay, endDay);
        return new MemberBlock(
                activities.days().stream().map(activity -> new Block(activity.date().getDayOfMonth(), activity.activeTime())).toList(),
                activities.totalActiveTime()
        );
    }
}
