package com.whoz_in.main_api.query.open_api.member.presentation.docs;

import com.whoz_in.main_api.query.open_api.member.application.daily.DailyMembers;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원", description = "회원 Api")
public interface MemberQueryApi {

    @Operation(
            summary = "금일 재실 기록 조회",
            description = """
                    금일 재실한 회원의 기록을 조회합니다.
                    """
    )
    ResponseEntity<SuccessBody<DailyMembers>> getActiveMembers(
            // 날짜 넣어야 함
    );
}
