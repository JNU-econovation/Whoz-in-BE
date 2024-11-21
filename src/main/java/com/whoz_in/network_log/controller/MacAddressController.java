package com.whoz_in.network_log.controller;

import com.whoz_in.network_log.common.ApiResponse;
import com.whoz_in.network_log.domain.managed.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/macs")
@RequiredArgsConstructor
public class MacAddressController {

    private final LogRepository logRepository;

    @GetMapping
    public ApiResponse.SuccessBody getMacAddress() {
        return
    }

}
