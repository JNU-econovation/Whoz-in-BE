package com.whoz_in.main_api.query.shared.application;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public final class QueryHandlers {
    Map<Class<? extends Query>, QueryHandler> queryHandlers;

    //스프링 빈 중 QueryHandler인 것들을 찾고, 꺼내서 쓰기 쉽게 Map으로 변경합니다.
    public QueryHandlers(ApplicationContext context) {
        queryHandlers =
                context.getBeansOfType(QueryHandler.class).values().stream()
                        .collect(
                                Collectors.toMap(
                                        queryHandler -> {
                                            //queryHandler의 프록시 객체를 가져옴 (예시 - @Transactional 사용 시)
                                            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(queryHandler);
                                            //QueryHandler의 제네릭 타입을 알아낸다.
                                            ParameterizedType paramType =
                                                    (ParameterizedType) Arrays.stream(targetClass.getGenericInterfaces())
                                                            .filter(ParameterizedType.class::isInstance)
                                                            .filter(type -> ((ParameterizedType) type).getRawType().equals(QueryHandler.class))
                                                            .findAny()
                                                            .orElseThrow(() -> new IllegalStateException("QueryHandler에 제네릭 타입이 존재하지 않음"));
                                            return (Class<? extends Query>) paramType.getActualTypeArguments()[0];
                                        },
                                        queryHandler -> queryHandler
                                )
                        );
    }

    //해당 Query를 처리하는 QueryHandler를 찾습니다.
    public QueryHandler findBy(Query query){
        QueryHandler queryHandler = queryHandlers.get(query.getClass());
        if (null == queryHandler) throw new IllegalStateException(query.getClass().getName() + "를 처리할 핸들러를 찾을 수 없습니다.");
        return queryHandler;
    }
}
