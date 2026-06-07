package com.whoz_in.main_api.query.open_api.member.application.by_date;

import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class MembersByDateHandler implements QueryHandler<MembersByDateGet, MembersByDate> {
    private final MembersByDateViewer membersByDateViewer;

    @Override
    public MembersByDate handle(MembersByDateGet query) {
        if (query.isSingleDate()) {
            return new MembersByDate(List.of(
                    membersByDateViewer.findAllByDate(query.date())
            ));
        }

        return new MembersByDate(membersByDateViewer.findAllByYearMonth(query.yearMonth()));
    }
}
