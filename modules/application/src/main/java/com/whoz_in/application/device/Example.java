package com.whoz_in.application.device;


import com.whoz_in.application.shared.query.Response;
import lombok.Getter;

@Getter
public final class Example implements Response {
    private final String goodResponse = "성공응답";
    public String get(){
        return "성공";
    }
}
