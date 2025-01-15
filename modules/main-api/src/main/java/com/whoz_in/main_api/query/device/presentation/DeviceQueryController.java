package com.whoz_in.main_api.query.device.presentation;

import com.whoz_in.main_api.query.device.application.active.ActiveDeviceList;
import com.whoz_in.main_api.query.device.application.active.ActiveDeviceListResponse;
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

    protected DeviceQueryController(QueryBus queryBus) {
        super(queryBus);
    }

    @GetMapping("/devices/active")
    public ResponseEntity<SuccessBody<ActiveDeviceListResponse>> getActiveDevices(
            @RequestParam("size") int size,
            @RequestParam("page") int page,
            @RequestParam("sortType") String sortType
    ) {
        ActiveDeviceList query = new ActiveDeviceList(page, size, sortType);
        ActiveDeviceListResponse response = ask(query);
        return ResponseEntityGenerator.success(response, CrudResponseCode.READ);
    }
  
    @GetMapping("/device/info-status")
    public ResponseEntity<SuccessBody<TempDeviceInfosStatus>> getTempDeviceInfosStatus(@RequestParam String room, @RequestParam String ip){
        return ResponseEntityGenerator.success(
                ask(new TempDeviceInfosStatusGet(room, ip)),
                CrudResponseCode.READ);
    }

}