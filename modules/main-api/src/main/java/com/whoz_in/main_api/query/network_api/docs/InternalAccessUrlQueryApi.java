package com.whoz_in.main_api.query.network_api.docs;

import com.whoz_in.main_api.query.member.application.response.MembersInRoomResponse;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "동방 내부 아이피 url", description = "동방 내부 아이피 url Api")
public interface InternalAccessUrlQueryApi {

    @Operation(
            summary = "동방 내부 아이피 url 조회",
            description = "해당 동방의 network-api에 접속할 수 있는 내부 아이피로 구성된 url을 제공합니다."
    )
    ResponseEntity<SuccessBody<String>> getInternalAccessUrl(
            @Parameter(name="room") String room
    );
}
