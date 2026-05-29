package com.whoz_in.main_api.query.api.ranking.presentation.docs;

import com.whoz_in.main_api.query.api.ranking.presentation.Rankings;
import com.whoz_in.main_api.shared.enums.RankingType;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "랭킹", description = "랭킹 Api")
public interface RankingQueryApi {

    @Operation(
            summary = "랭킹 조회 api",
            description = """
                    여러 랭킹을 반환합니다.
                    """
    )
    ResponseEntity<SuccessBody<Rankings>> getRankings(
            @Parameter(name="categories", required = true)List<RankingType> categories,
            @Parameter(name="n") Integer n,
            @Parameter(name="generation") Integer generation
    );
}
