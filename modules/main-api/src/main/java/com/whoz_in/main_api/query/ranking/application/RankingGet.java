package com.whoz_in.main_api.query.ranking.application;

import com.whoz_in.main_api.query.shared.application.Query;
import com.whoz_in.main_api.shared.enums.RankingType;
import com.whoz_in.shared.Nullable;

public record RankingGet(
        RankingType rankingType,
        int n,
        @Nullable Integer generation
) implements Query {

    public RankingGet(RankingType rankingType, Integer n, Integer generation) {
        this(rankingType, n == null ? 3 : n, generation);
    }
}
