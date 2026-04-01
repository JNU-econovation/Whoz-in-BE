package com.whoz_in.main_api.config.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class KakaoPromptAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
    private static final String AUTHORIZATION_REQUEST_BASE_URI = "/oauth2/authorization";
    private static final String KAKAO_REGISTRATION_ID = "kakao";
    private static final String PROMPT_PARAMETER_NAME = "prompt";
    private static final String LOGIN_PROMPT_VALUE = "login";

    private final OAuth2AuthorizationRequestResolver delegate;

    public KakaoPromptAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.delegate = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository,
                AUTHORIZATION_REQUEST_BASE_URI
        );
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        return customize(request, delegate.resolve(request), extractRegistrationId(request));
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        return customize(request, delegate.resolve(request, clientRegistrationId), clientRegistrationId);
    }

    private OAuth2AuthorizationRequest customize(
            HttpServletRequest request,
            OAuth2AuthorizationRequest authorizationRequest,
            String clientRegistrationId
    ) {
        if (authorizationRequest == null) return null;
        if (!KAKAO_REGISTRATION_ID.equals(clientRegistrationId)) return authorizationRequest;
        if (!LOGIN_PROMPT_VALUE.equals(request.getParameter(PROMPT_PARAMETER_NAME))) return authorizationRequest;

        Map<String, Object> additionalParameters = new HashMap<>(authorizationRequest.getAdditionalParameters());
        additionalParameters.put(PROMPT_PARAMETER_NAME, LOGIN_PROMPT_VALUE);

        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(additionalParameters)
                .build();
    }

    private String extractRegistrationId(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        int baseUriIndex = requestUri.indexOf(AUTHORIZATION_REQUEST_BASE_URI);
        if (baseUriIndex < 0) return null;

        String path = requestUri.substring(baseUriIndex + AUTHORIZATION_REQUEST_BASE_URI.length());
        if (path.startsWith("/")) path = path.substring(1);

        int nextSlashIndex = path.indexOf('/');
        return nextSlashIndex >= 0 ? path.substring(0, nextSlashIndex) : path;
    }
}
