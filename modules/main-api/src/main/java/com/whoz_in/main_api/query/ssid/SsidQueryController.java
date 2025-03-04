package com.whoz_in.main_api.query.ssid;

import com.whoz_in.main_api.config.RoomSsidConfig;
import com.whoz_in.main_api.query.ssid.docs.SsidQueryApi;
import com.whoz_in.main_api.shared.presentation.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SsidQueryController implements SsidQueryApi {
    private final RoomSsidConfig ssidConfig;
    @GetMapping("/ssid")
    public ResponseEntity<SuccessBody<List<String>>> getSsidList(){
        return ResponseEntityGenerator.success(ssidConfig.getSsids(), CrudResponseCode.READ);
    }
}
