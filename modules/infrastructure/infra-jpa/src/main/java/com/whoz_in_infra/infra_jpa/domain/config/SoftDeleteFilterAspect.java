package com.whoz_in_infra.infra_jpa.domain.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;


@Aspect
@Component("domainSoftDeleteFilterAspect")
public class SoftDeleteFilterAspect {
    @PersistenceContext(unitName = "domain")
    private EntityManager entityManager;

    @Around("execution(* com.whoz_in_infra.infra_jpa.domain..*(..)) && @annotation(com.whoz_in_infra.infra_jpa.shared.WithDeleted)")
    public Object aroundWithDeleted(ProceedingJoinPoint joinPoint) throws Throwable {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.getEnabledFilter("softDeleteFilter");
        boolean wasFilterEnabled = (filter != null);
        try {
            if (wasFilterEnabled) {
                session.disableFilter("softDeleteFilter");
            }
            return joinPoint.proceed();
        } finally {
            if (wasFilterEnabled) {
                session.enableFilter("softDeleteFilter");
            }
        }
    }
}
