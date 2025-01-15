package com.whoz_in.main_api.query.device.presentation;

import com.whoz_in.main_api.query.device.application.TempDeviceInfosStatus;
import com.whoz_in.main_api.query.device.application.TempDeviceInfosStatusGet;
import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.presentation.QueryController;
import com.whoz_in.main_api.shared.presentation.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DeviceQueryController extends QueryController {

    public DeviceQueryController(QueryBus queryBus) {
        super(queryBus);
    }

    @GetMapping("/device/info-status")
    public ResponseEntity<SuccessBody<TempDeviceInfosStatus>> getTempDeviceInfosStatus(@RequestParam String room, @RequestParam String ip){
        return ResponseEntityGenerator.success(
                ask(new TempDeviceInfosStatusGet(room, ip)),
                CrudResponseCode.READ);
    }
}
