package com.whoz_in.main_api.query.api.ranking.application;

import com.whoz_in.main_api.query.shared.application.Viewer;
import com.whoz_in.main_api.shared.enums.RankingType;
import java.util.List;
import java.util.UUID;

public interface RankingViewer extends Viewer {
    List<RankingMember> findRanking(RankingType type, int n, Integer generation, UUID finderId);
    List<Integer> findAvailableGenerations(RankingType type);
}
