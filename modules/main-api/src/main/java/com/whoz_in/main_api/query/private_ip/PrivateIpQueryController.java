package com.whoz_in.main_api.query.private_ip;

import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.presentation.QueryController;
import com.whoz_in.main_api.shared.presentation.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PrivateIpQueryController extends QueryController {
    public PrivateIpQueryController(QueryBus queryBus) {
        super(queryBus);
    }

    @GetMapping("/private-ip")
    public ResponseEntity<SuccessBody<PrivateIpList>> getPrivateIps(@RequestParam(required = false) String room){
        return ResponseEntityGenerator.success(ask(new PrivateIpListGet(room)), CrudResponseCode.READ);
    }
}
