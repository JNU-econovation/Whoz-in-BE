package com.whoz_in.network_log.controller.util;

import jakarta.servlet.http.HttpServletRequest;

public class HttpUtils {

    public static String extractIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

}
