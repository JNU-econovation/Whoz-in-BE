package com.whoz_in.network_log.infrastructure.controller;

import com.whoz_in.network_log.application.log.manager.MulticastDNSLogManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/network")
public class NetworkInfoController {

    private final MulticastDNSLogManager networkLogSaver;

    @GetMapping
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("OK");
    }

}