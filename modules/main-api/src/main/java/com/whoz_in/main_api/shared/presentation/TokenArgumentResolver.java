package com.whoz_in.main_api.shared.presentation;

import com.whoz_in.main_api.shared.jwt.tokens.Token;
import com.whoz_in.main_api.shared.jwt.tokens.TokenException;
import com.whoz_in.main_api.shared.jwt.tokens.TokenSerializer;
import com.whoz_in.main_api.shared.jwt.tokens.TokenType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public final class TokenArgumentResolver implements HandlerMethodArgumentResolver {

    private final List<TokenSerializer<? extends Token>> serializers;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Token.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {

        Class<?> paramType = parameter.getParameterType();

        TokenSerializer<?> matchedSerializer = serializers.stream()
                .filter(s -> {
                    ResolvableType type = ResolvableType.forClass(s.getClass()).as(TokenSerializer.class);
                    return type.hasGenerics() && type.getGeneric(0).resolve() == paramType;
                })
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No TokenSerializer for " + paramType.getSimpleName()));

        TokenType tokenType = TokenType.findByClass(paramType);

        String tokenStr = resolveTokenString(webRequest, tokenType.getCookieName());
        if (tokenStr == null) {
            throw new TokenException("2001", tokenType + "이 없음");
        }

        return matchedSerializer.deserialize(tokenStr)
                .orElseThrow(() -> new TokenException("2002", "유효하지 않은 토큰: " + tokenType));
    }

    private String resolveTokenString(NativeWebRequest webRequest, String cookieName) {
        // Authorization 헤더 우선 확인
        String header = webRequest.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        // 그리고 쿠키 검사
        Cookie[] cookies = ((HttpServletRequest) webRequest.getNativeRequest()).getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
