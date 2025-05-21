package com.whoz_in.domain_jpa.shared;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SoftDeleteFilterAspect {
    private final EntityManager entityManager;

    @Around("@annotation(WithDeleted)")
    public Object includeDeleted(ProceedingJoinPoint joinPoint) throws Throwable {
        Session session = entityManager.unwrap(Session.class);

        // 필터가 켜져 있다면 parameter를 false로 바꿔 일시적으로 삭제된 것도 조회
        Filter filter = session.getEnabledFilter("softDeleteFilter");
        boolean hadFilter = filter != null;

        if (hadFilter) {
            filter.setParameter("enabled", false);
        }

        try {
            return joinPoint.proceed();
        } finally {
            if (hadFilter) {
                session.enableFilter("softDeleteFilter").setParameter("enabled", true);
            }
        }
    }
}
