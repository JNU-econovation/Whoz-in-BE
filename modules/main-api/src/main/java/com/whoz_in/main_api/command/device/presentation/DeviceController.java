package com.whoz_in.main_api.command.device.presentation;

import com.whoz_in.main_api.command.device.application.DeviceInfoEdit;
import com.whoz_in.main_api.command.device.application.DeviceInfoTempAdd;
import com.whoz_in.main_api.command.device.application.DeviceInfoTempAddRes;
import com.whoz_in.main_api.command.device.application.DeviceRegister;
import com.whoz_in.main_api.command.device.application.DeviceRemove;
import com.whoz_in.main_api.command.device.presentation.docs.DeviceCommandApi;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.command.shared.presentation.CommandController;
import com.whoz_in.main_api.shared.jwt.tokens.DeviceRegisterToken;
import com.whoz_in.main_api.shared.presentation.logging.LogBody;
import com.whoz_in.main_api.shared.presentation.response.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.response.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class DeviceController extends CommandController implements DeviceCommandApi {
    public DeviceController(CommandBus commandBus) {
        super(commandBus);
    }

    @LogBody
    @PostMapping("/device/info")
    public ResponseEntity<SuccessBody<DeviceInfoTempAddRes>> addDeviceInfo(
            DeviceRegisterToken token,
            @RequestBody DeviceInfoTempAddReq req) {
        return ResponseEntityGenerator.success(
                dispatch(new DeviceInfoTempAdd(token, req.ip(), req.ssidHint())), CrudResponseCode.CREATE);
    }

    @PatchMapping("/device/info")
    public ResponseEntity<SuccessBody<Void>> updateDeviceInfo(@RequestBody DeviceInfoEdit request){
        return ResponseEntityGenerator.success(dispatch(request), CrudResponseCode.CREATE);
    }

    @PostMapping("/device")
    public ResponseEntity<SuccessBody<Void>> registerDevice(@RequestBody DeviceRegister request) {
        dispatch(request);
        return ResponseEntityGenerator.success(CrudResponseCode.CREATE);
    }

    @DeleteMapping("/device")
    public ResponseEntity<SuccessBody<Void>> removeDevice(@RequestBody DeviceRemove request) {
        dispatch(request);
        return ResponseEntityGenerator.success(CrudResponseCode.DELETE);
    }
}
