package com.whoz_in.api.device.application;


import com.whoz_in.api.shared.application.Handler;
import com.whoz_in.api.shared.application.query.QueryHandler;

@Handler
public final class ExampleGetHandler extends QueryHandler<ExampleGet, Example> {

    @Override
    public Example handle(ExampleGet query) {
        return new Example();
    }
}
