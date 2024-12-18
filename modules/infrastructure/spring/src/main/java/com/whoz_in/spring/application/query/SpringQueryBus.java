package com.whoz_in.spring.application.query;

import com.whoz_in.main_api.shared.application.query.Query;
import com.whoz_in.main_api.shared.application.query.QueryBus;
import com.whoz_in.main_api.shared.application.query.QueryHandler;
import com.whoz_in.main_api.shared.application.query.Response;
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
