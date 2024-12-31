package com.whoz_in.main_api.query.shared.application;

public interface QueryBus {
    <R extends Response> R ask(Query query);
}
