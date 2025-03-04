package com.whoz_in.network_api.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.UnknownHostException;
import org.springframework.http.ResponseEntity;

@Tag(name = "Network", description = "Network API")
public interface NetworkApi {

    @Operation(
            summary = "아이피 가져오기",
            description = "Http Request에서 아이피를 가져오는 API"
    )
    ResponseEntity<String> getIp() throws UnknownHostException;

    @Operation(
            summary = "방 이름 조회",
            description = "어떤 동아리 방인지 가져오기"
    )
    ResponseEntity<String> getRoom();

}
