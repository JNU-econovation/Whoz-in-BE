package com.whoz_in.main_api.query.api.room_access_url;

import com.whoz_in.main_api.query.api.room_access_url.docs.RoomAccessUrlQueryApi;
import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.presentation.QueryController;
import com.whoz_in.main_api.shared.application.caching.network_api.RoomAccessUrlStore;
import com.whoz_in.main_api.shared.presentation.response.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.response.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RoomAccessUrlQueryController extends QueryController implements RoomAccessUrlQueryApi {
    private final RoomAccessUrlStore roomAccessUrlStore;

    public RoomAccessUrlQueryController(QueryBus queryBus, RoomAccessUrlStore roomAccessUrlStore) {
        super(queryBus);
        this.roomAccessUrlStore = roomAccessUrlStore;
    }

    @GetMapping("/room-access-url")
    @Override
    public ResponseEntity<SuccessBody<String>> getRoomAccessUrl(@RequestParam String room) {
        return roomAccessUrlStore.get(room).map(
                url -> ResponseEntityGenerator.success(url, CrudResponseCode.READ)
        ).orElseThrow(() -> new NoRoomAccessUrlException(room));
        // TODO: 추후 main 서버 시작 시 이벤트를 발행하여 바로 업데이트될 수 있도록 한다.
    }
}
