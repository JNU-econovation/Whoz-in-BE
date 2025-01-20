package com.whoz_in.main_api.command.device.presentation;

import com.whoz_in.main_api.command.device.application.DeviceInfoAdd;
import com.whoz_in.main_api.command.device.application.DeviceInfoEdit;
import com.whoz_in.main_api.command.device.application.DeviceRegister;
import com.whoz_in.main_api.command.device.application.DeviceRemove;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.command.shared.presentation.CommandController;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<SuccessBody<String>> addDeviceInfo(@RequestBody DeviceInfoAdd request) {
        return ResponseEntityGenerator.success(dispatch(request), "맥 정보 등록 완료", HttpStatus.CREATED);
    }

    @PatchMapping("/device/info")
    public ResponseEntity<SuccessBody<Void>> updateDeviceInfo(@RequestBody DeviceInfoEdit request){
        return ResponseEntityGenerator.success(dispatch(request), "맥 정보 수정 완료", HttpStatus.OK);
    }

    @PostMapping("/device")
    public ResponseEntity<SuccessBody<Void>> registerDevice(@RequestBody DeviceRegister request) {
        dispatch(request);
        return ResponseEntityGenerator.success("기기 등록 완료", HttpStatus.CREATED);
    }

    @DeleteMapping("/device")
    public ResponseEntity<SuccessBody<Void>> removeDevice(@RequestBody DeviceRemove request) {
        dispatch(request);
        return ResponseEntityGenerator.success("기기 삭제 완료", HttpStatus.NO_CONTENT);
    }
}
