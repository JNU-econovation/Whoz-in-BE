package com.whoz_in.network_api.config.interceptor;

import com.whoz_in.network_api.common.util.RequesterInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class ClientIpInterceptor implements HandlerInterceptor {

    private final RequesterInfo requesterInfo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        requesterInfo.setIp(request.getRemoteAddr());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        requesterInfo.clear();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
