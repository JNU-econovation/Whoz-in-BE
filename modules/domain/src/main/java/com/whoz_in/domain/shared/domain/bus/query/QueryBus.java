package com.whoz_in.domain.shared.domain.bus.query;

public interface QueryBus {
    <R> R ask(Query query);
}
