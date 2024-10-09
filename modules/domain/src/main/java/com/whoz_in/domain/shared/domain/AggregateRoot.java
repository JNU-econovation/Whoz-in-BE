package com.whoz_in.domain.shared.domain;

import com.whoz_in.domain.shared.domain.bus.event.DomainEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//애그리거트 루트는 무조건 상속해야 한다.
public abstract class AggregateRoot {
    private List<DomainEvent> domainEvents = new ArrayList<>();

    public final List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = domainEvents;
        this.domainEvents = Collections.emptyList();
        return events;
    }

    //AggregateRoot 구현체만 이벤트를 등록할 수 있으므로 protected
    protected final void register(DomainEvent event) {
        domainEvents.add(event);
    }
}