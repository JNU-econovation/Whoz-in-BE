package com.whoz_in.domain_jpa.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;


@Aspect
@Component("domainSoftDeleteFilterAspect")
@RequiredArgsConstructor
public class SoftDeleteFilterAspect {
    @PersistenceContext(unitName = "domain_jpa")
    private final EntityManager entityManager;

    @Around("execution(* com.whoz_in.domain_jpa..*(..)) && @annotation(com.whoz_in.shared_jpa.WithDeleted)")
    public Object aroundWithDeleted(ProceedingJoinPoint joinPoint) throws Throwable {
        Session session = entityManager.unwrap(Session.class);
//        System.out.println(">> 트랜잭션 활성 상태: " + TransactionSynchronizationManager.isActualTransactionActive());
        Filter filter = session.getEnabledFilter("softDeleteFilter");
        boolean wasFilterEnabled = (filter != null);
//        System.out.println(
//                "session.getEnabledFilter(\"softDeleteFilter\") = " + session.getEnabledFilter(
//                        "softDeleteFilter"));
        try {
            if (wasFilterEnabled) {
                session.disableFilter("softDeleteFilter");
            }
//            System.out.println(
//                    "zz" + session.getEnabledFilter(
//                            "softDeleteFilter"));
            // 없애야 함
//            session.enableFilter("softDeleteFilter");
//            System.out.println(">> session = " + session);  // AOP에서 꺼낸 session
            //            System.out.println(">> session = " + session);  // 동일한 세션인지?
            return joinPoint.proceed();
        } finally {
            if (wasFilterEnabled) {
//                System.out.println(
//                        "session.getEnabledFilter(\"softDeleteFilter\") = " + session.getEnabledFilter(
//                                "softDeleteFilter"));
                session.enableFilter("softDeleteFilter");
//                System.out.println(
//                        "session.getEnabledFilter(\"softDeleteFilter\") = " + session.getEnabledFilter(
//                                "softDeleteFilter"));
            }
        }
    }
}
