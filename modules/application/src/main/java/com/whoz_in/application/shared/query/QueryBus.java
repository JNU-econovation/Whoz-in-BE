package com.whoz_in.application.shared.query;

public interface QueryBus {
    <R extends Response> R ask(Query query);
}
