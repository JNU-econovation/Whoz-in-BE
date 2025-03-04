package com.whoz_in.main_api.query.ssid.docs;

import com.whoz_in.main_api.shared.presentation.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "SSID", description = "SSID Query Api")
public interface SsidQueryApi {

    @Operation(
            summary = "SSID 리스트 조회",
            description = "SSID 리스트를 조회합니다."
    )
    ResponseEntity<SuccessBody<List<String>>> getSsidList();

}
