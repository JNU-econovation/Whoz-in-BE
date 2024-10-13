package com.whoz_in.application.device;


import com.whoz_in.application.shared.Handler;
import com.whoz_in.application.shared.query.QueryHandler;

@Handler
public final class ExampleGetHandler extends QueryHandler<ExampleGet, Example> {

    @Override
    public Example handle(ExampleGet query) {
        return new Example();
    }
}
