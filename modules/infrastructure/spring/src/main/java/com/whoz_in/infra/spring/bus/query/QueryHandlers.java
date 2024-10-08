package com.whoz_in.infra.spring.bus.query;

import com.whoz_in.domain.shared.domain.bus.query.Query;
import com.whoz_in.domain.shared.domain.bus.query.QueryHandler;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.stream.Collectors;
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
                                            Class<? extends QueryHandler> handlerClass = queryHandler.getClass();
                                            ParameterizedType paramType = (ParameterizedType) handlerClass.getGenericSuperclass();
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
