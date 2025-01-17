package com.whoz_in.master_api.private_ip;

import com.whoz_in.main_api.command.private_ip.PrivateIpUpdate;
import com.whoz_in.main_api.command.private_ip.PrivateIpUpdateHandler;
import com.whoz_in.network_api.private_ip.PrivateIpWriter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

//main-api의 핸들러를 직접 호출하여 내부 아이피를 최신화함
@Primary
@Component
@RequiredArgsConstructor
public class MainApiHandlerPrivateIpWriter implements PrivateIpWriter {
    private final PrivateIpUpdateHandler privateIpUpdateHandler;
    @Override
    public void write(String room, Map<String, String> privateIps) {
        privateIpUpdateHandler.handle(new PrivateIpUpdate(room, privateIps));
    }
}
