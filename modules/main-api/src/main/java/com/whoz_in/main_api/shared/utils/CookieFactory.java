package com.whoz_in.main_api.shared.utils;

import jakarta.servlet.http.Cookie;
import java.time.Duration;

public abstract class CookieFactory {
    //브라우저가 꺼지면 사라지는 쿠키
    public abstract Cookie create(String name, String value);

    //만료 시간이 설정된 쿠키
    public Cookie create(String name, String value, int expiry){
        Cookie cookie = create(name, value);
        cookie.setMaxAge(expiry);
        return cookie;
    }
    
    public Cookie create(String name, String value, Duration expiry){
        return create(name, value, (int) expiry.toSeconds());
    }

    //브라우저의 쿠키를 삭제하는 쿠키
    public Cookie createDeletionCookie(String name){
        return create(name, null, 0);
    }
}
