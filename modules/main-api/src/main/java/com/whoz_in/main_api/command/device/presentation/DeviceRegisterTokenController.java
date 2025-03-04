package com.whoz_in.main_api.command.device.presentation;

import com.whoz_in.main_api.command.device.presentation.docs.DeviceRegisterTokenApi;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.shared.jwt.tokens.DeviceRegisterToken;
import com.whoz_in.main_api.shared.jwt.tokens.TokenSerializer;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 브라우저에서 내부 아이피에서 쿠키 전송이 불가능하기 때문에 만들어짐
// 브라우저의 요구사항이므로 application까지 넘기지 않았다.
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DeviceRegisterTokenController implements DeviceRegisterTokenApi {
    private final TokenSerializer<DeviceRegisterToken> deviceRegisterTokenSerializer;
    private final RequesterInfo requesterInfo;

    @PostMapping("/device-register-token")
    public ResponseEntity<SuccessBody<String>> getDeviceRegisterToken() {
        return ResponseEntityGenerator.success(
                deviceRegisterTokenSerializer.serialize(
                        new DeviceRegisterToken(requesterInfo.getMemberId())),
                "기기 등록 토큰 발급 완료",
                HttpStatus.OK
        );
    }
}