package com.whoz_in.network_api.controller;

import com.whoz_in.network_api.config.NetworkApiFrontendUrlProvider;
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
public class NetworkApiFrontendUrlController {
    private final NetworkApiFrontendUrlProvider networkApiFrontendUrlProvider;

    @GetMapping("/frontend-url")
    public ResponseEntity<String> getUrl() {
        return ResponseEntity.ok(networkApiFrontendUrlProvider.get());
    }
}
