package com.whoz_in.main_api.command.network_api;

import com.whoz_in.main_api.command.network_api.docs.CorsOriginCommandApi;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.command.shared.presentation.CommandController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//server to server
@RestController
@RequestMapping("/internal/api/v1")
public class CorsOriginController extends CommandController implements
        CorsOriginCommandApi {

    public CorsOriginController(CommandBus commandBus) {
        super(commandBus);
    }

    @PutMapping("/cors-origin")
    public void updateCors(@RequestBody CorsOriginUpdate req){
        dispatch(req);
    }
}
