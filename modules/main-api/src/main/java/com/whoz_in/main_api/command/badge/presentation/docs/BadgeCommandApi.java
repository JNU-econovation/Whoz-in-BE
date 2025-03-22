package com.whoz_in.main_api.command.badge.presentation.docs;

import com.whoz_in.main_api.command.badge.application.BadgeAttach;
import com.whoz_in.main_api.command.badge.application.BadgeRegister;
import com.whoz_in.main_api.command.badge.application.SwitchBadgeVisibility;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Badge", description = "Badge Command Api")
public interface BadgeCommandApi {

    @Operation(
            summary ="뱃지 생성",
            description = "사용자는 대회/공모전/자격증 뱃지를 생성할 수 있습니다. 이는 12시간 후 반영됩니다."
    )
    ResponseEntity<SuccessBody<Void>> create(@RequestBody BadgeRegister request);

    @Operation(
            summary = "뱃지 달기",
            description = "사용자는 등록 가능한 뱃지를 조회하고 뱃지를 자신의 것으로 달 수 있습니다."
    )
    ResponseEntity<SuccessBody<Void>> attach(@RequestBody BadgeAttach request);

    @Operation(
            summary = "뱃지 보여주기 또는 숨기기",
            description = "사용자는 뱃지를 보여주고 숨김으로써 자신의 뱃지 조회 상태를 관리할 수 있습니다."
    )
    ResponseEntity<SuccessBody<Void>> update(@RequestBody SwitchBadgeVisibility request);
}
