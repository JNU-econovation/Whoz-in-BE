package com.whoz_in.main_api.command.device.presentation;

import com.whoz_in.main_api.command.device.application.DeviceRegister;
import com.whoz_in.main_api.command.device.application.DeviceInfoAdd;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.command.shared.presentation.CommandController;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class DeviceController extends CommandController{
    public DeviceController(CommandBus commandBus) {
        super(commandBus);
    }

    @PostMapping("/device/info")
    public ResponseEntity<SuccessBody<Void>> addDeviceInfo(@RequestBody DeviceInfoAdd request) {
        dispatch(request);
        return null;
    }

    @PostMapping("/device")
    public ResponseEntity<SuccessBody<Void>> registerDevice(@RequestBody DeviceRegister request) {
        dispatch(request);
        return null;
    }

    //사용자 정보 조회 (filter가 많아. 예를 들어, 가입순, 최근 동방나온순, ....)

    //뱃지 정보를 조회 (List<memberid>)
    //자세한 뱃지 정보 조회 (memberId) -
}
