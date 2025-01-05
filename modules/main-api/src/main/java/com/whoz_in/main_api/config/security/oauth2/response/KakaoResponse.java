package com.whoz_in.main_api.config.security.oauth2.response;

import java.util.Map;

public class KakaoResponse implements ProviderResponse{

    private final Map<String, Object> attributes;

    @SuppressWarnings("unchecked")
    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getSocialId() {
        return attributes.get("id").toString();
    }
}
