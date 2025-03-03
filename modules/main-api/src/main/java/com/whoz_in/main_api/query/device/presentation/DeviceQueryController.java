package com.whoz_in.main_api.query.device.presentation;

import com.whoz_in.main_api.query.device.application.DevicesStatus;
import com.whoz_in.main_api.query.device.application.DevicesStatusGet;
import com.whoz_in.main_api.query.device.application.TempDeviceInfosStatus;
import com.whoz_in.main_api.query.device.application.TempDeviceInfosStatusGet;
import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.presentation.QueryController;
import com.whoz_in.main_api.shared.presentation.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name="기기", description = "기기 조회에 관한 API")
public class DeviceQueryController extends QueryController {

    protected DeviceQueryController(QueryBus queryBus) {
        super(queryBus);
    }

    @Operation(
            summary = "기기 상태 조회",
            description = "기기 Id, 기기 mac, 기기 이름, ssid를 조회합니다."
    )
    @GetMapping("/devices")
    public ResponseEntity<SuccessBody<DevicesStatus>> getDeviceStatus(){
        return ResponseEntityGenerator.success(ask(new DevicesStatusGet()), CrudResponseCode.READ);
    }


    @Operation(
            summary = "기기 상태 조회",
            description = "기기 등록을 위한 임시적인 기기들의 상태 정보를 조회합니다."
    )
    @GetMapping("/device/info-status")
    public ResponseEntity<SuccessBody<TempDeviceInfosStatus>> getTempDeviceInfosStatus(@RequestParam String room, @RequestParam String ip){
        return ResponseEntityGenerator.success(
                ask(new TempDeviceInfosStatusGet(room, ip)),
                CrudResponseCode.READ);
    }

}