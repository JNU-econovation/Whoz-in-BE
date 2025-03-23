package com.whoz_in.main_api.query.badge.presentatiion.docs;

import com.whoz_in.main_api.query.badge.application.response.BadgeInfoResponse;
import com.whoz_in.main_api.query.badge.application.view.BadgesOfMember;
import com.whoz_in.main_api.query.badge.application.view.RegistrableBadges;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "뱃지", description = "뱃지 Query Api")
public interface BadgeQueryApi {

    @Operation(
            summary = "뱃지 아이디로 뱃지 정보 조회",
            description = "뱃지에 관한 정보(이름, 색상코드)를 조회합니다."
    )
    ResponseEntity<SuccessBody<BadgeInfoResponse>> viewBadgeInfo(@RequestParam("badgeId") UUID badgeId);

    @Operation(
            summary = "등록 가능한 뱃지 조회",
            description = "사용자가 달 수 있는 등록 가능한 뱃지를 조회합니다. 등록 가능한 뱃지는 개발단에서 만든 뱃지가 아닌 후즈인 사용자가 만든 뱃지를 말합니다."
    )
    ResponseEntity<SuccessBody<RegistrableBadges>> viewRegisterable();

    @Operation(
            summary = "사용자의 뱃지 조회",
            description = "사용자가 가지고 있는 뱃지와 조회 가능 상태를 조회합니다."
    )
    ResponseEntity<SuccessBody<BadgesOfMember>> viewBadgesOfMember();
}
