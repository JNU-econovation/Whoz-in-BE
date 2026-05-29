package com.whoz_in.main_api.query.api.room_access_url.docs;

import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "특정 동방 서버에 요청할 수 있는 url(사설 아이피)", description = "특정 동방 서버에 요청할 수 있는 url(사설 아이피) Api")
public interface RoomAccessUrlQueryApi {

    @Operation(
            summary = "특정 동방 서버에 요청할 수 있는 url(사설 아이피) 조회",
            description = "요청한 동방의 network-api에 요청할 수 있는 url을 제공합니다."
    )
    ResponseEntity<SuccessBody<String>> getRoomAccessUrl(
            @Parameter(name="room") String room
    );
}
