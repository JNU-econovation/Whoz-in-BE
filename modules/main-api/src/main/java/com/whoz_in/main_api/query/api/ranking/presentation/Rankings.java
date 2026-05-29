package com.whoz_in.main_api.query.api.ranking.presentation;

import com.whoz_in.main_api.query.api.ranking.application.Ranking;
import com.whoz_in.main_api.shared.enums.RankingType;
import java.util.Map;

public record Rankings(
        Map<RankingType, Ranking> rankings
) {}
