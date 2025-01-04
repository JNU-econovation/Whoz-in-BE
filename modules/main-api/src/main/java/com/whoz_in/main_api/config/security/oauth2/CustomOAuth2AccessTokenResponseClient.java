package com.whoz_in.main_api.config.security.oauth2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

// 임시 AuthCode 로 OAuth 제공자로부터 AccessToken 을 받아오는 Client
@Component
public class CustomOAuth2AccessTokenResponseClient implements
        OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        String registrationId = request.getClientRegistration().getRegistrationId();

        // 액세스 토큰 요청
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                request.getClientRegistration().getProviderDetails().getTokenUri(),
                HttpMethod.POST,
                new HttpEntity<>(createRequestBody(request), createHeaders(request)),
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        // 응답 파싱
        Map<String, Object> tokenResponse = response.getBody();
        if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token_response", "Token response is invalid", null));
        }

        return handle(registrationId, tokenResponse);
    }

    private OAuth2AccessTokenResponse handle(String registrationId, Map<String, Object> tokenResponse) {
        if(registrationId==null){
            throw new IllegalStateException("등록되지 않은 OAuth 제공자");
        }

        // TODO: OAuth Provider 가 많아지면 Switch-Case 로 가능
        if (registrationId.equals("kakao")) {
            return handleKakaoAccessTokenResponse(tokenResponse);
        }

        return OAuth2AccessTokenResponse.withToken((String) tokenResponse.get("access_token"))
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(((Number) tokenResponse.get("expires_in")).longValue())
                .refreshToken((String) tokenResponse.get("refresh_token"))
                .scopes(parseScopesWithComma((String) tokenResponse.get("scope")))
                .build();
    }

    private OAuth2AccessTokenResponse handleKakaoAccessTokenResponse(Map<String, Object> tokenResponse) {
        return OAuth2AccessTokenResponse.withToken((String) tokenResponse.get("access_token"))
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(((Number) tokenResponse.get("expires_in")).longValue())
                .refreshToken((String) tokenResponse.get("refresh_token"))
                .scopes(parseScopesWithComma((String) tokenResponse.get("scope")))
                .build();
    }

    private MultiValueMap<String, String> createRequestBody(OAuth2AuthorizationCodeGrantRequest request) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add("grant_type", request.getGrantType().getValue());
        formParameters.add("code", request.getAuthorizationExchange().getAuthorizationResponse().getCode());
        formParameters.add("redirect_uri", request.getClientRegistration().getRedirectUri());
        formParameters.add("client_id", request.getClientRegistration().getClientId());
        formParameters.add("client_secret", request.getClientRegistration().getClientSecret());
        return formParameters;
    }

    private HttpHeaders createHeaders(OAuth2AuthorizationCodeGrantRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private Set<String> parseScopesWithComma(String scope) {
        return scope != null ? new HashSet<>(Arrays.asList(scope.split(","))) : Collections.emptySet();
    }
}
