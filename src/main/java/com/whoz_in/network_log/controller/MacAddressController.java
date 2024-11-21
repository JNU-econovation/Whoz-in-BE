package com.whoz_in.network_log.controller;

import com.whoz_in.network_log.common.ApiResponse;
import com.whoz_in.network_log.common.util.RequesterInfo;
import com.whoz_in.network_log.controller.dto.MacResponse;
import com.whoz_in.network_log.controller.util.HttpUtils;
import com.whoz_in.network_log.domain.managed.repository.LogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/macs")
@RequiredArgsConstructor
public class MacAddressController {

    private final LogRepository logRepository;
    private final RequesterInfo requesterInfo;

    @GetMapping
    public ApiResponse.SuccessBody getMacAddress() {
        String ip = requesterInfo.getIp();
        System.out.println("Requester Info : " + requesterInfo.getIp());

        MacResponse response = new MacResponse(logRepository.findByIp(ip).getLogId().getMac());

        return ApiResponse.success(HttpStatus.OK, "조회 성공", response);
    }
}
