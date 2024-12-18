package com.whoz_in.main_api.query.device_info.presentation;

import com.whoz_in.main_api.query.device_info.application.DeviceInfoGet;
import com.whoz_in.main_api.shared.application.command.CommandBus;
import com.whoz_in.main_api.shared.application.query.QueryBus;
import com.whoz_in.main_api.shared.presentation.CommandQueryController;
import com.whoz_in.main_api.shared.presentation.response.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.response.ResponseEntityGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceQueryController extends CommandQueryController {

    protected DeviceQueryController(CommandBus commandBus, QueryBus queryBus) {
        super(commandBus, queryBus);
    }

    @GetMapping("/device")
    public ResponseEntity<?> getDevice(){
        ask(new DeviceInfoGet(1L));
        return ResponseEntityGenerator.success(CrudResponseCode.CREATE);
    }
}
