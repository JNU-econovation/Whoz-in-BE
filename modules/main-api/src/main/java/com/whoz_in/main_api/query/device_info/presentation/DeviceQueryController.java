package com.whoz_in.main_api.query.device_info.presentation;

import com.whoz_in.main_api.query.device_info.application.DeviceInfoGet;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.presentation.QueryController;
import com.whoz_in.main_api.shared.presentation.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceQueryController extends QueryController {

    public DeviceQueryController(QueryBus queryBus) {
        super(queryBus);
    }

    @GetMapping("/device")
    public ResponseEntity<?> getDevice(){
        ask(new DeviceInfoGet(1L));
        return ResponseEntityGenerator.success(CrudResponseCode.CREATE);
    }
}
