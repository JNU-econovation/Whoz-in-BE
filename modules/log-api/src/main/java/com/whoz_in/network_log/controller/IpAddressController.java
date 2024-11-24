package com.whoz_in.network_log.controller;

import com.whoz_in.network_log.common.util.RequesterInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class IpAddressController {
    private final RequesterInfo requesterInfo;

    @GetMapping("/ip")
    public ResponseEntity<String> getMac() {
        String ip = requesterInfo.getIp();
        System.out.println("Requester Info : " + ip);
        return ResponseEntity.ok(ip);
    }

}
