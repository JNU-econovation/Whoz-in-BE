package com.whoz_in.main_api.query.api.ranking.presentation;

import com.whoz_in.main_api.query.api.ranking.application.Ranking;
import com.whoz_in.main_api.query.api.ranking.application.RankingGet;
import com.whoz_in.main_api.query.api.ranking.presentation.docs.RankingQueryApi;
import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.presentation.QueryController;
import com.whoz_in.main_api.shared.enums.RankingType;
import com.whoz_in.main_api.shared.presentation.response.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.response.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RankingQueryController extends QueryController implements RankingQueryApi {
    public RankingQueryController(QueryBus queryBus) {
        super(queryBus);
    }

    @GetMapping("/rankings")
    public ResponseEntity<SuccessBody<Rankings>> getRankings(
            @RequestParam("categories") List<RankingType> categories,
            @RequestParam(value = "n", required = false) Integer n,
            @RequestParam(value = "generation", required = false) Integer generation
    ) {
        Map<RankingType, Ranking> rankingMap = new HashMap<>();
        for (RankingType type : categories) {
            RankingGet query = new RankingGet(type, n, generation);
            Ranking result = ask(query);
            rankingMap.put(type, result);
        }
        return ResponseEntityGenerator.success(new Rankings(rankingMap), CrudResponseCode.READ);
    }
}
