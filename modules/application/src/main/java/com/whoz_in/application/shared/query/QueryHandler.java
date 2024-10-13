package com.whoz_in.application.shared.query;

/*
형변환 없이 구현체마다 다른 Response를 제공하기 위해 제네릭을 사용했습니다.
 */
public abstract class QueryHandler<Q extends Query, R extends Response> {
    public abstract R handle(Q query);
}
