package com.whoz_in.main_api.query.private_ip.docs;

import com.whoz_in.main_api.query.private_ip.PrivateIps;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "private IP", description = "private IP Api")
public interface PrivateIpQueryApi {

    @Operation(
            summary = "private IP 조회",
            description = "private IP를 조회합니다."
    )
    ResponseEntity<SuccessBody<PrivateIps>> getPrivateIps(@RequestParam(required = false) String room);

}
