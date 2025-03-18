package com.whoz_in.main_api.command.network_api;

import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.command.shared.presentation.CommandController;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//server to server
// network-api가 이 api를 사용하여 자신의 Internal Access Ssid의 ip를 알릴 수 있다.
@RestController
@RequestMapping("/internal/api/v1")
public class InternalAccessUrlController extends CommandController {

    public InternalAccessUrlController(CommandBus commandBus) {
        super(commandBus);
    }

    @PutMapping("/internal-access-url")
    public void updateCors(@Valid @RequestBody InternalAccessUrlUpdate req){
        dispatch(req);
    }
}
