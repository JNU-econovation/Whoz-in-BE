package com.whoz_in.main_api.query.ranking.application;

import java.util.Collection;
import java.util.List;

public record TopNRanking(
    Collection<RankingMember> members,
    List<Integer> generations // 조회 가능한 기수
) implements Ranking {}
