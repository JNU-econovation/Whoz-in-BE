package com.whoz_in.main_api.query.api.ranking.application;

import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.util.Collection;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class RankingHandler implements QueryHandler<RankingGet, Ranking> {
    private final RequesterInfo requesterInfo;
    private final RankingViewer viewer;

    @Override
    public TopNRanking handle(RankingGet query) {
        Collection<RankingMember> members = viewer.findRanking(
                query.rankingType(),
                query.n(),
                query.generation(),
                requesterInfo.getMemberId().id()
        );

        return new TopNRanking(members, viewer.findAvailableGenerations(query.rankingType()));
    }
}
