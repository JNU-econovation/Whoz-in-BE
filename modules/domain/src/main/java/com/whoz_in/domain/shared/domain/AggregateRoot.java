package com.whoz_in.domain.shared.domain;

import com.whoz_in.domain.shared.domain.bus.event.DomainEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot {
    //애그리거트 루트는 자신에 관한 도메인 이벤트를 가지고 있습니다.
    private List<DomainEvent> domainEvents = new ArrayList<>();

    //응용 로직에게 발행할 이벤트를 제공합니다.
    public final List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = domainEvents;
        this.domainEvents = Collections.emptyList();
        return events;
    }

    //애그리거트 루트의 변경은 자신이 직접 관리해야 하므로 protected입니다.
    protected final void register(DomainEvent event) {
        domainEvents.add(event);
    }
}