package com.whoz_in.main_api.command.device.presentation.docs;

import com.whoz_in.main_api.command.device.application.DeviceInfoEdit;
import com.whoz_in.main_api.command.device.application.DeviceInfoTempAddRes;
import com.whoz_in.main_api.command.device.application.DeviceRegister;
import com.whoz_in.main_api.command.device.application.DeviceRemove;
import com.whoz_in.main_api.command.device.presentation.DeviceInfoTempAddReq;
import com.whoz_in.main_api.shared.jwt.tokens.DeviceRegisterToken;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Device", description = "Device Command API")
public interface DeviceCommandApi {

    @Operation(
            summary = "기기 Mac 정보 등록",
            description = "기기의 Mac 주소 등록을 위한 API, 해당 아이피로 등록된 와이파이들을 리스트로 반환한다."
    )
    ResponseEntity<SuccessBody<DeviceInfoTempAddRes>> addDeviceInfo(
            DeviceRegisterToken token, DeviceInfoTempAddReq request);


    @Operation(
            summary = "기기 정보 수정",
            description = "기기 정보 수정을 위한 API"
    )
    ResponseEntity<SuccessBody<Void>> updateDeviceInfo(@RequestBody DeviceInfoEdit request);

    @Operation(
            summary = "기기 등록",
            description = "기기 등록을 위한 API"
    )
    ResponseEntity<SuccessBody<Void>> registerDevice(@RequestBody DeviceRegister request);

    @Operation(
            summary = "기기 삭제",
            description = "기기 삭제을 위한 API"
    )
    ResponseEntity<SuccessBody<Void>> removeDevice(@RequestBody DeviceRemove request);

}
