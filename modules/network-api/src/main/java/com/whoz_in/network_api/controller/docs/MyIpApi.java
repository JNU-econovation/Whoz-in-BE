package com.whoz_in.network_api.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.UnknownHostException;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Ip", description = "Ip API")
public interface MyIpApi {

    @Operation(
            summary = "연결된 와이파이에서 나의 내부 아이피 가져오기",
            description = "Http Request에서 아이피를 가져오는 API"
    )
    ResponseEntity<String> getIp() throws UnknownHostException;

    // network-api마다 요청할 아이피가 다르니 서버에서 관리하는 것임
    @Operation(
            summary = "/my-ip를 요청할 수 있는 아이피들 가져오기",
            description = "/my-ip를 통해 자신의 내부 아이피를 얻기 위해선 적절한 아이피로 요청해야 합니다."
    )
    public ResponseEntity<List<String>> getAccessIps();
}
