package com.whoz_in.main_api.query.device.presentation.docs;

import com.whoz_in.main_api.query.device.application.DevicesStatus;
import com.whoz_in.main_api.query.device.application.TempDeviceInfosStatus;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "기기", description = "기기 조회 API")
public interface DeviceQueryApi {

    @Operation(
            summary = "기기 상태 조회",
            description = "기기 Id, 기기 mac, 기기 이름, ssid를 조회합니다."
    )
    public ResponseEntity<SuccessBody<DevicesStatus>> getDeviceStatus();

    @Operation(
            summary = "기기 상태 조회",
            description = "기기 등록을 위한 임시적인 기기들의 상태 정보를 조회합니다."
    )
    ResponseEntity<SuccessBody<TempDeviceInfosStatus>> getTempDeviceInfosStatus(@RequestParam String room, @RequestParam String ip);

}
