package com.whoz_in.main_api.command.network_api.docs;

import com.whoz_in.main_api.command.network_api.CorsOriginUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Network Api Cors Origin", description = "Network Api Cors Origin Api")
public interface CorsOriginCommandApi {

    @Operation(
            summary = "cors origin 추가",
            description = "Internal Access Ssid의 ip가 변경됐을경우 cors origin을 추가"
    )
    void updateCors(@RequestBody CorsOriginUpdate req);

}
