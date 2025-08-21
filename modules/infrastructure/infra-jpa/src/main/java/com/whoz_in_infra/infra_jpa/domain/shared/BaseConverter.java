package com.whoz_in_infra.infra_jpa.domain.shared;

//domain 모델(AggregateRoot)과 infra-jpa 모델(JpaEntity) 간 상호변환 기능을 담당한다.
//domain-jpa의 Converter이므로 JpaEntity를 기준으로 메서드명을 작명하였다.
public abstract class BaseConverter<T extends BaseEntity, R> {
    public abstract T from(R r);
    public abstract R to(T t);
}
