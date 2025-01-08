package com.whoz_in.main_api.shared.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "https-enabled", havingValue = "true")
@Qualifier("secure")
@Component
public class HttpsCookieFactory extends CookieFactory{
    @Override
    public Cookie create(String name, String value){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "None");
        return cookie;
    }
}
