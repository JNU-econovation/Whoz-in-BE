package com.whoz_in.domain.device.application;

import com.whoz_in.domain.shared.domain.bus.query.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public final class ExampleGetHandler extends QueryHandler<ExampleGet, Example> {

    @Override
    public Example handle(ExampleGet query) {
        return new Example();
    }
}
