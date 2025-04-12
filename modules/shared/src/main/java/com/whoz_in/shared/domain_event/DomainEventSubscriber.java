package com.whoz_in.shared.domain_event;


public abstract class DomainEventSubscriber<E extends DomainEvent> {
    public abstract void on(E event);
}
