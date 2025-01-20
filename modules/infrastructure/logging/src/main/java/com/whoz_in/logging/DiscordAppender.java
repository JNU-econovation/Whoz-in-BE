package com.whoz_in.logging;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class DiscordAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private final CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionTimeToLive(10, TimeUnit.SECONDS)
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setSocketTimeout(5000)
                    .build())
            .build();

    //null인 필드는 body에 추가하지 않도록 함
    private final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(
            JsonInclude.Include.NON_NULL);

    private String webhookUri;
    private Layout<ILoggingEvent> layout;
    private PatternLayoutEncoder encoder;
    private String username;
    private String avatarUrl;

    @Override
    public void start() {
        if (webhookUri == null || webhookUri.isBlank()) {
            addError("Webhook URI가 없음");
            return;
        }

        if (layout == null){
            if (encoder != null) {
                layout = encoder.getLayout();
            } else {
                addError("layout이나 encoder가 설정되지 않음");
                return;
            }
        }
        super.start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        try {
            // layout 적용
            String message = layout.doLayout(eventObject);
            // 메세지 가공
            message = cleanseMessage(message);
            // 디스코드 웹훅 명세에 맞게 json으로 직렬화
            String jsonPayload = makeJsonPayload(message);
            // 디스코드로 전송
            send(jsonPayload);
        } catch (Exception e) {
            addError("디스코드 웹훅 요청 실패: ", e);
        }
    }

    private String makeJsonPayload(String message) throws JsonProcessingException {
        // body 만들기
        Map<String, Object> payload = new HashMap<>();
        payload.put("content", message);
        payload.put("username", username);
        payload.put("avatar_url", avatarUrl);

        return objectMapper.writeValueAsString(payload);
    }

    private void send(String jsonPayload) throws IOException {
        HttpPost post = new HttpPost(webhookUri);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(jsonPayload, "UTF-8"));

        try (CloseableHttpResponse response = httpClient.execute(post)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 204 && statusCode != 200) { //성공 시 204를 보내긴 하던데 일단 200도 추가
                addError("" + statusCode);
            }
        }
    }

    // 디스코드는 2000자 이하만 보낼 수 있음
    private String cleanseMessage(String message) {
        message = message.replaceAll("```\\s```", "");
        if (message.length() > 2000) {
            message = message.substring(0, 1997) + "...";
        }
        return message;
    }

    //Logback XML 설정을 위한 Getter, Setter
    //이 Appender 재사용할 수 있으니 Lombok 안썼음
    public Layout<ILoggingEvent> getLayout() {
        return layout;
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }

    public String getWebhookUri() {
        return webhookUri;
    }

    public void setWebhookUri(String webhookUri) {
        this.webhookUri = webhookUri;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
