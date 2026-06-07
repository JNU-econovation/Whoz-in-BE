package com.whoz_in.main_api.query.open_api.member.presentation.docs;

import com.whoz_in.main_api.query.open_api.member.application.by_date.MembersByDate;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "공개 회원", description = "공개 회원 Api")
public interface MemberQueryApi {

    @Operation(
            summary = "날짜 기준 재실 기록 조회",
            description = """
                    요청한 날짜 또는 월에 재실 기록이 있는 회원 목록을 조회합니다.
                    """
    )
    ResponseEntity<SuccessBody<MembersByDate>> getMembersByDate(
            @Parameter(name = "year", description = "조회 연도", example = "2026") int year,
            @Parameter(name = "month", description = "조회 월", example = "6") int month,
            @Parameter(name = "day", description = "조회 일자. 없으면 해당 월 전체를 조회합니다.", example = "5") Integer day
    );
}
