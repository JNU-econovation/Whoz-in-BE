package com.whoz_in.main_api.query.shared.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class InMemoryQueryBus implements QueryBus {
    private final QueryHandlers handlers;

    @Override
    public <R extends Response> R ask(Query query){
        QueryHandler<Query, R> handler = handlers.findBy(query);
        return handler.handle(query);
    }
}
