package com.whoz_in.main_api.command.private_ip.docs;

import com.whoz_in.main_api.command.private_ip.PrivateIpUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Private IP", description = "Private IP Api")
public interface PrivateIpCommandApi {

    @Operation(
            summary = "private IP 변경",
            description = "동아리방의 private IP를 변경합니다."
    )
    void updatePrivateIp(@RequestBody PrivateIpUpdate req);

}
