package com.whoz_in.network_api.controller;

import com.whoz_in.network_api.common.util.IpHolder;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import com.whoz_in.network_api.controller.docs.NetworkApi;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class NetworkApiController implements NetworkApi {
    private final IpHolder ipHolder;
    private final List<String> gateways;
    private final String room;

    public NetworkApiController(
            IpHolder ipHolder,
            @Value("${room-setting.room-name}") String room,
            NetworkInterfaceProfileConfig profileConfig) {
        this.ipHolder = ipHolder;
        this.room = room;
        this.gateways = profileConfig.getManagedProfiles().stream()
                .map(profile-> profile.ni().getNetworkAddress().gateway())
                .toList();
    }

    @GetMapping("/ip")
    public ResponseEntity<String> getIp() throws UnknownHostException {
        String ip = ipHolder.getIp();
        log.info("Requester Info : " + ip);
        if (gateways.contains(ip) || !InetAddress.getByName(ip).isSiteLocalAddress()){ // 루프백도 외부 아이피로 간주된다.
            return ResponseEntity.badRequest().body("외부 아이피로 요청됨");
        }
        return ResponseEntity.ok(ip);
    }

    @GetMapping("/room")
    public ResponseEntity<String> getRoom(){
        return ResponseEntity.ok(room);
    }
}
