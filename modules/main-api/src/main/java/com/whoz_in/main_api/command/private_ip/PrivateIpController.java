package com.whoz_in.main_api.command.private_ip;

import com.whoz_in.main_api.command.private_ip.docs.PrivateIpCommandApi;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.command.shared.presentation.CommandController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//server to server
@RestController
@RequestMapping("/internal/api/v1")
public class PrivateIpController extends CommandController implements PrivateIpCommandApi {

    public PrivateIpController(CommandBus commandBus) {
        super(commandBus);
    }

    @PutMapping("/private-ip")
    public void updatePrivateIp(@RequestBody PrivateIpUpdate req){
        dispatch(req);
    }
}
