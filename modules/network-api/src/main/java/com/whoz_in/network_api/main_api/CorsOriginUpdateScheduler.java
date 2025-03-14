package com.whoz_in.network_api.main_api;

import com.whoz_in.network_api.config.NetworkApiFrontendUrlProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


// 주기적으로 메인 api의 cors origin을 업데이트함 (network-api에서 서빙하는 기기 등록 페이지에서 main-api에 접근하므로)
// TODO: 변경됐을때만 업데이트하도록 수정
@Slf4j
@Component
public final class CorsOriginUpdateScheduler {
    private final String roomName;
    private final CorsWriter writer;
    private final NetworkApiFrontendUrlProvider networkApiFrontendUrlProvider;

    public CorsOriginUpdateScheduler(
            @Value("${room-name}") String roomName,
            CorsWriter writer,
            NetworkApiFrontendUrlProvider networkApiFrontendUrlProvider
    ) {
        this.roomName = roomName;
        this.writer = writer;
        this.networkApiFrontendUrlProvider = networkApiFrontendUrlProvider;
    }

    @Scheduled(fixedRate = 30000)
    private void update() {
        writer.write(roomName, networkApiFrontendUrlProvider.get());
    }
}
