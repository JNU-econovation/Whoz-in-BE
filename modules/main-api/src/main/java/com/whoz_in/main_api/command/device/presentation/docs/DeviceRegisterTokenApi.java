package com.whoz_in.main_api.command.device.presentation.docs;

import com.whoz_in.main_api.shared.presentation.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Device Register Token", description = "Device Register Token Api")
public interface DeviceRegisterTokenApi {


    @Operation(
            summary = "임시 토큰 발급",
            description = "기기 등록을 위한 임시 토큰을 발급합니다."
    )
    ResponseEntity<SuccessBody<String>> getDeviceRegisterToken();

}
