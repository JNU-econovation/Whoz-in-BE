package com.whoz_in.network_log.controller;

import com.whoz_in.network_log.common.ApiResponse;
import com.whoz_in.network_log.common.util.RequesterInfo;
import com.whoz_in.network_log.controller.dto.MacResponse;
import com.whoz_in.network_log.domain.managed.repository.LogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MacAddressController {

    private final LogRepository logRepository;
    private final RequesterInfo requesterInfo;

    @GetMapping("/mac")
    public ApiResponse.SuccessBody<?> getMac() {
        String ip = requesterInfo.getIp();
        System.out.println("Requester Info : " + ip);

        return ApiResponse.success(HttpStatus.OK, "조회 성공", new MacResponse(logRepository.findByIp(ip).getLogId().getMac()));
    }

    @GetMapping("/macs")
    public ApiResponse.SuccessBody<?> getMacs(){
        String ip = requesterInfo.getIp();
        System.out.println("Requester Info : " + ip);

        return ApiResponse.success(HttpStatus.OK, "조회 성공", logRepository.findAllByIp(ip).stream().map(log->log.getLogId().getMac()).toList());
    }

    @GetMapping("/ip")
    public String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        String httpXForwardedFor = request.getHeader("HTTP-X-Forwarded-For");
        String httpXForwardedFor2 = request.getHeader("HTTP_X_Forwarded_For");
        String xRealIp = request.getHeader("X-Real-IP");
        String remoteAddr = request.getRemoteAddr();

        StringBuilder result = new StringBuilder();
        result.append("X-Forwarded-For: ").append(xForwardedFor).append("\n");
        result.append("HTTP-X-Forwarded-For: ").append(httpXForwardedFor).append("\n");
        result.append("HTTP-X-Forwarded-For2: ").append(httpXForwardedFor2).append("\n");
        result.append("X-Real-IP: ").append(xRealIp).append("\n");
        result.append("RemoteAddr: ").append(remoteAddr).append("\n");

        return result.toString();
    }
}
