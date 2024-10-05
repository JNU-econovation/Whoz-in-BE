package domain.shared.domain.bus.query;

/*
형변환 없이 구현체마다 다른 Response를 제공하기 위해 제네릭 사용
 */
public interface QueryHandler<Q extends Query, R extends Response> {
    R handle(Q query);
}
