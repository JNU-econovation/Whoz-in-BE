package com.whoz_in.infra.spring.bus.query;

import com.whoz_in.domain.shared.domain.bus.query.Query;
import com.whoz_in.domain.shared.domain.bus.query.QueryBus;
import com.whoz_in.domain.shared.domain.bus.query.QueryHandler;
import com.whoz_in.domain.shared.domain.bus.query.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class SpringQueryBus implements QueryBus {
    private final QueryHandlers handlers;

    @Override
    public <R extends Response> R ask(Query query){
        QueryHandler<Query, R> handler = handlers.findBy(query);
        return handler.handle(query);
    }
}
