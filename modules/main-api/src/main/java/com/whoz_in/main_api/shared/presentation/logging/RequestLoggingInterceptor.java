package com.whoz_in.main_api.shared.presentation.logging;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestLoggingInterceptor implements HandlerInterceptor {
    private final RequesterInfo requesterInfo;
    private final MemberViewer memberViewer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        requesterInfo.findMemberId()
                .map(MemberId::id)
                .ifPresentOrElse(
                        id -> {
                            memberViewer.findNameByMemberId(id)
                                    .ifPresentOrElse(
                                            memberInfo -> log.info("[REQUESTER] id: {}, name: {}", id, memberInfo.memberName()),
                                            () -> log.warn("[REQUESTER] id: {}", id) // 쿼리에 회원 정보가 없을 경우
                                    );
                        },
                        () -> log.info("[REQUESTER] 알 수 없음 (비인증 사용자)")
                );
        return true;
    }
}
