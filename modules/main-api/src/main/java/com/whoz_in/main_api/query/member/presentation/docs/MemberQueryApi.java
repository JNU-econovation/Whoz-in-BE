package com.whoz_in.main_api.query.member.presentation.docs;

import com.whoz_in.main_api.query.member.application.request.MembersInRoomRequest;
import com.whoz_in.main_api.query.member.application.response.MembersInRoomResponse;
import com.whoz_in.main_api.query.member.application.view.MemberDetailInfo;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원", description = "회원 Api")
public interface MemberQueryApi {

    @Operation(
            summary = "동아리 방 현황 조회",
            description = "동아리 방에 누가 있는지 현황을 조회합니다."
    )
    ResponseEntity<SuccessBody<MembersInRoomResponse>> getActiveDevices(@ParameterObject MembersInRoomRequest query);


    @Operation(
            summary = "회원 상세정보 조회",
            description = "회원 상세정보 (Id, 이름, 기수, 포지션, 상태메세지) 를 조회합니다."
    )
    ResponseEntity<SuccessBody<MemberDetailInfo>> getDetailInfo();

}
