package com.whoz_in.api.shared.application.query;

public interface QueryBus {
    <R extends Response> R ask(Query query);
}
