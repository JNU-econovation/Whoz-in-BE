package com.whoz_in.network_api.controller;

import com.whoz_in.network_api.common.util.RequesterInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class IpAddressController {
    private final RequesterInfo requesterInfo;

    @GetMapping("/ip")
    public ResponseEntity<String> getMac() {
        String ip = requesterInfo.getIp();
        log.info("Requester Info : " + ip);
        return ResponseEntity.ok(ip);
    }

}
