package com.whoz_in.domain.device.application;

import com.whoz_in.domain.shared.domain.bus.query.Response;
import lombok.Getter;

@Getter
public final class Example implements Response {
    private final String response = "성공응답";
    public String get(){
        return "성공";
    }
}
