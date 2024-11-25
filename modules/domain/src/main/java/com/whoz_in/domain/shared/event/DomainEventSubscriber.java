package com.whoz_in.domain.shared.event;

public abstract class DomainEventSubscriber<E extends DomainEvent> {
    public abstract void on(E event);
}
