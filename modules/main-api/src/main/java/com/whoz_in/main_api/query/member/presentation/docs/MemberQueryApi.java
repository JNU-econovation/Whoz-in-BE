package com.whoz_in.main_api.query.member.presentation.docs;

import com.whoz_in.main_api.query.member.application.block.MemberBlock;
import com.whoz_in.main_api.query.member.application.detail.MemberDetail;
import com.whoz_in.main_api.query.member.application.in_room.MembersInRoom;
import com.whoz_in.main_api.query.member.application.profile.MemberProfile;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "회원", description = "회원 Api")
public interface MemberQueryApi {

    @Operation(
            summary = "동아리 방 현황 조회",
            description = """
                    동아리 방 현황을 조회합니다.
                    """
    )
    ResponseEntity<SuccessBody<MembersInRoom>> getActiveMembers(
            @Parameter(name="size") int size,
            @Parameter(name="page") int page
    );

    @Operation(
            summary = "회원 상세정보 조회",
            description = "자신의 상세정보를 조회합니다."
    )
    ResponseEntity<SuccessBody<MemberDetail>> getDetailInfo();

    @Operation(
            summary = "회원 프로필 조회",
            description = "특정 회원의 정보를 조회합니다."
    )
    ResponseEntity<SuccessBody<MemberProfile>> getProfileInfo(@PathVariable("memberId") String memberId);

    @Operation(
            summary = "회원의 블록 조회",
            description = "특정 회원의 블록(한 달 단위 재실 현황)을 조회합니다."
    )
    ResponseEntity<SuccessBody<MemberBlock>> getBlock(
            @PathVariable("memberId") String memberId,
            @RequestParam("year") int year,
            @RequestParam("month") int month
    );
}
