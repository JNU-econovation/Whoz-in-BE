package com.whoz_in.main_api.query.network_api;

import com.whoz_in.main_api.query.network_api.docs.InternalAccessUrlQueryApi;
import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.presentation.QueryController;
import com.whoz_in.main_api.shared.caching.network_api.InternalAccessUrlStore;
import com.whoz_in.main_api.shared.presentation.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class InternalAccessUrlQueryController extends QueryController implements InternalAccessUrlQueryApi {
    private final InternalAccessUrlStore internalAccessUrlStore;

    public InternalAccessUrlQueryController(QueryBus queryBus, InternalAccessUrlStore internalAccessUrlStore) {
        super(queryBus);
        this.internalAccessUrlStore = internalAccessUrlStore;
    }

    @GetMapping("/internal-access-url")
    @Override
    public ResponseEntity<SuccessBody<String>> getInternalAccessUrl(@RequestParam String room) {
        return internalAccessUrlStore.get(room).map(
                url -> ResponseEntityGenerator.success(url, CrudResponseCode.READ)
        ).orElseThrow(() -> new NoInternalAccessUrlException(room));
        // TODO: 추후 main 서버 시작 시 이벤트를 발행하여 바로 업데이트될 수 있도록 한다.
    }
}
