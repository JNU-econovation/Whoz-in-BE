package com.whoz_in.main_api.shared.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Qualifier("basic")
@Component
public class BasicCookieFactory extends CookieFactory{
    @Override
    public Cookie create(String name, String value){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "Lax");
        return cookie;
    }
}
