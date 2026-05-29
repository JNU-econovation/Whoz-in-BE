package com.whoz_in.network_api.main_api;

import com.whoz_in.network_api.config.NetworkApiFrontendUrlProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


// 주기적으로 메인 api에게 자신(n-api)의 RoomAccessUrl를 업데이트함
// TODO: main이 시작됐을때, url이 변경됐을때만 업데이트하도록 수정
@Slf4j
@Component
public final class RoomAccessUrlUpdateScheduler {
    private final String roomName;
    private final RoomAccessUrlWriter writer;
    private final NetworkApiFrontendUrlProvider networkApiFrontendUrlProvider;

    public RoomAccessUrlUpdateScheduler(
            @Value("${room-name}") String roomName,
            RoomAccessUrlWriter writer,
            NetworkApiFrontendUrlProvider networkApiFrontendUrlProvider
    ) {
        this.roomName = roomName;
        this.writer = writer;
        this.networkApiFrontendUrlProvider = networkApiFrontendUrlProvider;
    }

    @Scheduled(fixedRate = 30000)
    private void update() {
        networkApiFrontendUrlProvider.get().ifPresentOrElse(
                url-> writer.write(roomName, url),
                ()->log.warn("room access NI가 인터넷에 연결되어있지 않습니다.")
        );
    }
}
